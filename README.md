# Propositional Logic KB Inference via DPLL for Minesweeper

## Question 1. Formalize a minesweeper problem in propositional logic
#### Along with required files you may need to produce in each question, write your answers to **all questions** in parts (a) to (d) in the top-level file Part1.txt (provided in your repo). 
Use the following symbols along with parentheses for nesting when you write propositional sentences:

| Symbol | Meaning |
| ------ |:-------:|
| ~      | not     |
| ^      | and     |
| &#124; | or      |
| =>     | implication      |
| <=> | biconditional (equivalence) |

Here is an example propositional sentence: a => ( ( b <=> ( c | d ) ) ^ e )


#### Problem Description: Boolean Diagonal Minesweeper

There is a 3x3 grid. Each cell in the grid may have a mine or not. For each cell,
either it is observed or not. If a cell is observed, an indicator for that cell is true
iff that cell or any of its diagonally adjacent cells (northeast, northwest, southeast,
southwest) has a mine in it (note that the “or” here is a standard “disjunctive or”,
not an “exclusive or”).

In the following subquestions of Q1, our goal is not to play the game, but rather to state
known facts about the game along with axioms relating mines and observations that
we will *Tell* our propositional knowledge base.  Later in Q2 we will also come up
with queries to *Ask* our propositional knowledge base (e.g., is a certain cell safe?) 
to determine whether we can prove the queries are entailed by the knowledge base.
Such reasoning could be used in a full game-playing agent, but our focus in this assignment
is only on the knowledge representation and reasoning aspect.

(a) **[0.5 pts]** Define all binary variables required for a propositional encoding of this
Minesweeper problem. Give your propositional variables interpretable names
like `m_1_2` for the variable indicating whether a mine is in the cell (1, 2). As a
function of *n*, how many variables are needed to encode Minesweeper for a
grid of dimension *n* × *n*?

(b) **[0.5 pts]** Define the facts (propositional sentences) to encode that the cell (1, 2) is observed and indicates a mine is **not** present, that the 
cell (2, 1) is observed and indicates a mine is present, and that the remaining cells are unobserved.

(c) **[1.0 pts]** Define the axioms (propositional sentences) for this problem required to enforce consistency between all the variables. 
Note that corner, edge, and middle portions of the grid must be treated separately when determining adjacency, 
e.g., a corner only has one diagonally adjacent cell whereas the middle cell (2, 2) has four diagonally adjacent cells. 
Your constraint encoding should use ⇒ and/or ⇔ and thus should not be in CNF.

(d) **[0.5 pts]** Following the example in the course slides, provide the step-by-step CNF transformation of the shortest 
axiom from (c) that involves the proposition `m_1_1`.

(e) **[0.5 pts]** Write a program in the gen/GenMineNxNDimacs.java file provided that generates the DIMACS CNF file for (b) and (c) above for an arbitrary NxN grid and output the file data/Mine3x3.dimacs for N=3 and data/Mine10x10.dimacs for N=10.  Note that only the CNF for (c) changes as N changes; the CNF for (b) is invariant to N.  The specification of the DIMACS format is provided below.  

Your code is *not* required to do an automatic CNF transformation of (b) and (c) -- this would be challenging and is beyond the scope of this assignment.  Instead the program should attempt to semi-automate the process by noting there is a clear pattern to the CNF translation of axioms in (c) for different cells; the code should iterate over all cells (i,j) to instantiate the appropriate CNF for each cell.  Finally, you may choose how to assign numeric IDs to variables (please specify this mapping in the comments of the file -- see example below) and there is no requirement on the order that you list the clauses in your DIMACS file.

**DIMACS Format**: An input file starts with comments (each comment line starts
with `c`). The number of variables and the number of clauses is defined by the line

`p cnf variables clauses`

Each of the next lines specifies a clause: a positive literal is denoted by the corresponding number, and a negative literal is denoted by the corresponding negative
number. The last number in a line should be zero. For example,
<pre>
c A sample .cnf file.
c v_1 -> 1
c v_2 -> 2
c v_3 -> 3
p cnf 3 2
1 -3 0
2 3 -1 0
</pre>

See this [web page](https://fairmut3x.wordpress.com/2011/07/29/cnf-conjunctive-normal-form-dimacs-format-explained) for more information on the DIMACS format. 
You may also find this [large set of DIMACS benchmarks](https://www.cs.ubc.ca/~hoos/SATLIB/benchm.html) useful.

## Question 2. Implement a DPLL solver and apply it to prove KB "Ask" queries in the minesweeper problem
#### Along with required files you may need to produce in each question, write your answers to **all questions** in parts (b) to (e) in the top-level file Part2.txt (provided in your repo).
#### Aside from the code we've provided, you must write all DPLL code yourself. You may of course cite external URL sources for small snippets of code you find useful.  Submitting a DPLL algorithm that is not your own will result in disciplinary action.

(a) **[2.0 pts]** Write a DPLL solver in the provided sat/UnitPropDPLL.java file that extends sat/Solver.java, 
to determine whether the input DIMACS CNF file is unsatisfiable by implementing the unsat() method.
The code to read a DIMACS CNF file is already implemented in Solver.java. 
You should implement *unit clause propagation*, but pure symbol elimination is not required. 
Use a variable ordering that orders variables with lower IDs (in the DIMACS format) before variables with higher
IDs (note: this is the same ordering already implemented in sat/ExhaustiveDPLL.java that we've provided). 

(b) **[0.5 pts]** Formulate the following six queries, and encode them as unsatisfiability problems in the specified files. 
These files should augment the content in data/Mine3x3.dimacs from Q1(e). 
What additional CNF clause(s) did you augment your DIMACS file from Q1(e)
with in order to make each of these queries?  (Include both the propositional sentences and what they mean in English.)

| Number | Query                              | File |
| -----: |:---------------------------------- | ---  |
| 1 | A mine is located in corner cell (1, 1) | data/Mine3x3_q1.dimacs
| 2 | A mine is **not** located in corner cell (1, 1) | data/Mine3x3_q2.dimacs
| 3 | A mine is located in middle edge cell (3, 2)      |data/Mine3x3_q3.dimacs
| 4 | A mine is **not** located in middle edge cell (3, 2)      |data/Mine3x3_q4.dimacs
| 5 | A mine is located in at least one of the middle edge cells {(1, 2), (2, 1), (2, 3), (3, 2)} |data/Mine3x3_q5.dimacs
| 6 | A mine is **not** located in any of the middle edge cells {(1, 2), (2, 1), (2, 3), (3, 2)}|data/Mine3x3_q6.dimacs

(c) **[0.5 pts]** Use your DPLL solver to solve the queries in part (b). Report the results (satisfiable or unsatisfiable) 
for each query along with the running time in Part2.txt under the designated section.  Format your results so they can be easily read via github.

(d) **[2.0 pts]** Run the solver on data/mine_22_SAT.dimacs and data/mine_22_UNSAT.dimacs. Report the unsatisfiability result and the run-time 
in milliseconds for each file in Part2.txt. Consider an enhancement and write it in sat/EnhancedDPLL.java, 
reporting the unsatisfiability result and the run-time in milliseconds for data/mine_22_SAT.dimacs and data/mine_22_UNSAT.dimacs in Part2.txt. 
sat/EnhancedDPLL.java should have at least 10% less run-time than sat/UnitPropDPLL.java 
(i.e. run-time(EnhancedDPLL) < 0.90 * run-time(UnitPropDPLL)) on one of the two files.  Briefly describe your enhancements in English in Part2.txt and why you chose them.

**[1.5 pts]** Competitive portion.  Assuming your code above is correct, we will time your sat/EnhancedDPLL.java on 10 different (large) DIMACS files.  Your competitive portion grade will be in the range [0,1.5] scaled linearly with 0 points for taking greater than or equal to the time of our solution for sat/UnitPropDPLL.java and 1.5 points for the fastest solution among correct submissions.

(e) **[0.5 pts]** If the inference system cannot prove that a cell does not have a mine, does
this mean that the cell must have a mine? Defend your answer.


### Submission checklist
Make sure your final submission contains **all** of the following files and the latest version is accessible from your github repo URL:

| Item | File Path | Description |
| ----: | --------- | ----------- |
| 1    | Part1.txt | Your written answers to Question 1 (a) - (d) |
| 2    | Part2.txt | Your written answers / results to Question 2 (b) - (e) |
| 3    | data/Mine3x3.dimacs | The output file from Question 1 (e) |
| 4    | data/Mine10x10.dimacs | The output file from Question 1 (e) |
| 5    | data/Mine3x3_q1.dimacs | The output file from Question 2 (b) |
| 6    | data/Mine3x3_q2.dimacs | The output file from Question 2 (b) |
| 7    | data/Mine3x3_q3.dimacs | The output file from Question 2 (b) |
| 8    | data/Mine3x3_q4.dimacs | The output file from Question 2 (b) |
| 9    | data/Mine3x3_q5.dimacs | The output file from Question 2 (b) |
| 10   | data/Mine3x3_q6.dimacs | The output file from Question 2 (b) |
| 11   | src/main/java/gen/GenMineNxNDimacs.java | Your code for Question 1 (e) |
| 12   | src/main/java/sat/UnitPropDPLL.java | Your code for Question 2 (a) |
| 13   | src/main/java/sat/EnhancedDPLL.java | Your code for Question 2 (d) |
| 14   | Java files you've added | Helper Java files you've added that your code needs to run |

