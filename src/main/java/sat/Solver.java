package sat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * An abstract abstract superclass for all DPLL classes
 */
public abstract class Solver {

    protected String _sDIMACSFile;
    protected int _nClauses;
    protected int _nVars;

    /**
     * An array of clauses (represented as sets of Integers)
     */
    protected ArrayList<HashSet<Integer>> _clauses;

    /**
     * Truth assignment to each var (or null)
     */
    protected Boolean[] _assign;

    // A counter of how often each literal appears in an active clause
    protected int[]     _assignCount;

    /**
     * Constructor
     * Reads a DIMACS file and initializes the member variables accordingly
     * @param dimacs_file path to the input DIMACS file
     */
    public Solver(String dimacs_file) {
        _nClauses = -1;
        _nVars = -1;
        _clauses = new ArrayList<HashSet<Integer>>();
        _sDIMACSFile = dimacs_file;
        try {
            BufferedReader br = new BufferedReader(new FileReader(dimacs_file));
            String line = null;
            int clause_counter = 0;
            while ((line = br.readLine()) != null) {

                // Discard comments
                if (line.startsWith("c"))
                    continue;

                // Read the 'p' line and allocate space for clauses and var assignments
                String[] split = line.split("[\\s]");
                if (split[0].equals("p")) {
                    if (!split[1].equals("cnf")) {
                        System.out.println("Cannot handle non-cnf: '" + split[1] + "'");
                        System.exit(1); // Fail ungracefully
                    }

                    _nVars = Integer.parseInt(split[2]); // Hope this is really an integer

                    // Note: using 1-offset arrays so can directly index into var array
                    _assign = new Boolean[_nVars + 1]; // 0 index is a dummy index
                    _assignCount = new int[_nVars + 1];

                    _nClauses = Integer.parseInt(split[3]); // Hope this is really an integer
                    _clauses.clear();
                    for (int i = 0; i < _nClauses; i++)
                        _clauses.add(new HashSet<>());

                    continue;
                }

                // Must be a CNF line
                HashSet<Integer> cur_clause = _clauses.get(clause_counter);
                for (int index = 0; index < split.length - 1; index++)
                    cur_clause.add(Integer.valueOf(split[index]));

                if (!split[split.length - 1].equals("0")) {
                    System.out.println("Clause line '" + line + "' did not end in 0.");
                    System.exit(1); // Fail ungracefully
                }
                ++clause_counter;
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error reading '" + dimacs_file + "':\n" + e);
            System.exit(1); // Fail ungracefully
        }
    }

    /**
     *  Display CNF info and current variable assignment
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nDIMACS File: '").append(_sDIMACSFile).append("'\n");
        sb.append("Variables: ").append(_nVars).append("\n");
        sb.append("Clauses:   ").append(_nClauses).append("\n");
        sb.append("Current assignment: ").append(getAssignString()).append("\n");
        int clause_id = 0;
        for (HashSet<Integer> clause : _clauses) {
            sb.append("#").append(clause_id++).append(": [");
            for (Integer literal : clause)
                sb.append(" ").append(literal);
            sb.append(" ]\n");

        }
        return sb.toString();
    }

    /**
     * Prints the current non-null assignments
     * NOTE: var_id's start at 1!
     * @return the string representation of assignments
     */
    private String getAssignString() {
        StringBuilder sb = new StringBuilder("[");
        for (int var = 1; var < _assign.length; var++)
            if (_assign[var] != null)
                sb.append(" ").append(var).append("=").append(_assign[var] ? "T" : "F");
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * The main entry point to the sat solver
     * @return true if clauses are unsatisfiable, false otherwise
     */
    public abstract boolean unsat();

}
