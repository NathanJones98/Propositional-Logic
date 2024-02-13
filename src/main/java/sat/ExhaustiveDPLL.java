package sat;

import java.util.*;

public class ExhaustiveDPLL extends Solver{

	/**
	 * Clause evaluation results
	 */
	enum Eval{
		TRUE,
		FALSE,
		INDET
	}

	/**
	 * Constructor
	 * @param dimacs_file path to the input DIMACS file
	 */
	public ExhaustiveDPLL(String dimacs_file) {
		super(dimacs_file);
	}

	// The main entry point to the sat solver... returns true if clauses are unsatisfiable.
	public boolean unsat() {
		HashSet<Integer> active_clauses = new HashSet<Integer>();
		for (int i = 0; i < _nClauses; i++)
			active_clauses.add(i); // When we start, all clauses are active (neither sat or unsat)
		int var_branch = chooseBranchVar();
		return dpllUnsat(var_branch, true, active_clauses)
			&& dpllUnsat(var_branch, false, active_clauses);
	}

	/**
	 * See if a clause evaluates to TRUE, FALSE, or INDET given current assignment in _assign.
	 * @param clause the clause to evaluate
	 * @return the clause evaluation result
	 */
	public Eval evaluateClause(HashSet<Integer> clause) {

		// Rule: if any literal is true, return EVAL.TRUE
		//       else if all literals false, return EVAL.FALSE
		//       else return EVAL_INDET
		boolean all_literals_false = true;
		for (Integer lit : clause) {
			boolean neg = lit < 0;
			Boolean assign = _assign[neg ? -lit : lit];
			if (assign == null) {
				all_literals_false = false; // We've found a literal that is unassigned
			} else if (assign == !neg) { // If lit neg=false, check if assign=true, vice versa if neg=true
				return Eval.TRUE; // Literal evaluates to true, so clause is satisfied
			}
		}

		// We only reach here if we've evaluated all literals and none evaluated to true
		return all_literals_false ? Eval.FALSE : Eval.INDET;
	}

	/**
	 * Copies assignments from _assign to a new Boolean array.
	 * Could be more efficient by preallocating these arrays for each level of the main.DPLL solution.
	 * @return array of assignments
	 */
	private Boolean[] copyAssignments() {
		Boolean[] new_assign = new Boolean[_assign.length];
		System.arraycopy(_assign, 0, new_assign, 0, _assign.length);
		return new_assign;
	}

	/**
	 * Choose the next unassigned variable to branch on
	 * @return the variable to branch on
	 */
	private int chooseBranchVar() {
		for (int var = 1; var <= _nVars; var++)
			if (_assign[var] == null)
				return var;
		return -1;
	}

	/**
	 * The standard main.DPLL interface, takes the next variable assignment and the
	 * set of currently unsatisfied clauses and returns whether they are
	 * unsatisfiable (true) or satisfiable (false).
	 *
	 * Note: One should typically take care *not* to call "new" (allocate memory) in this code,
	 * 	     but I have done so since I am aiming for clarity over efficiency.
	 * @param var_id variable to assign
	 * @param var_assign the boolean assignment to var_id
	 * @param active_clauses the currently unsatisfied clauses
	 * @return whether the clauses are unsatisfiable (true) or satisfiable (false).
	 */
	private boolean dpllUnsat(int var_id, boolean var_assign, HashSet<Integer> active_clauses) {

		// Prevents us from overwriting active_clauses
		active_clauses = (HashSet<Integer>)active_clauses.clone();

		// Make new assignment for var_id so we can undo before returning
		Boolean[] temp_assign = _assign;
		_assign = copyAssignments();
		_assign[var_id] = var_assign;

		// Filter out SAT clauses; detect UNSAT
		// modify the set of active_clauses and set unsat_detected to true if a clause was found to be unsatisfiable.
		for (Integer clause_id : (HashSet<Integer>)active_clauses.clone()) {
			Eval result = evaluateClause(_clauses.get(clause_id));
			if (result == Eval.FALSE)
				return true; // We found a FALSE clause => unsatisfiable = true
			else if (result == Eval.TRUE)
				active_clauses.remove(clause_id); // Satisfied, so no need to check again
		}

		if (active_clauses.isEmpty())
			return false; // Clause set can only be empty because *all* clauses were satisfied and removed

		// If we get here, not yet known whether unsat or sat, need to keep branching
		// Note: we don't need to check that we've exhausted all variable assignments, because
		//       by the time that happens all clauses will evaluate to either true or false
		int var_to_branch_on = chooseBranchVar();

		// Unsatisfiable only if both branches are unsatisfiable
		boolean unsat = dpllUnsat(var_to_branch_on, true, active_clauses)
			         && dpllUnsat(var_to_branch_on, false, active_clauses);

		// Undo assignment for var_id
		_assign = temp_assign;

		return unsat;
	}
}
