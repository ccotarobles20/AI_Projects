 Programming Assignment 3

Assigned: Mar. 4
Due: Apr. 3
Overview

In this assignment, you are to implement the Davis-Putnam algorithm and use it to solve a simple form of an adventure game.

The puzzle is as follows: You are given (1) a maze with treasures and tolls at particular nodes; (2) a maximum number of steps. The object is to find a path through the maze from START to GOAL in at most the given number of steps.

Example: Suppose that the maze is as follows:

Then the path "START, D, E, G, F, GOAL" succeeds in 5 steps. The path "START, D, A, B, A, D, C,GOAL" succeeds in 7 steps.

Assume that there is only one instance of each treasure in the maze. Therefore, each treasure is in one of three states: Either at its original location, or in the possession of the player, or spent as a toll. There may be more than one node that requires a particular treasure as a toll; if so, it is only possible to visit one of them.

A node may have more than one treasure as toll or may supply more than one treasure.

You may assume that START and GOAL have neither tolls nor treasures.

You will write three programs.

    An implementation of the Davis-Putnam procedure, which takes as input a set of clauses and outputs either a satisfying valuation, or a statement that the clauses cannot be satisfied.
    A front end, which takes as input a maze problem and outputs a set of clauses that can be input to (1).
    A back end, which takes as input the output of (1) and translates it into a solution to the original problem. 

Compiling the maze puzzle to propositional logic
The MAZE puzzle can be expressed propositionally as follows: There are three kinds of atoms:

    At(N,I) means that the player is on node N at time I. For instance At(C,2) means that the player is on node C at time 2. (Note: in propositional logic, a construction like "At(C,2)" is considered a single symbol of 7 characters. The parentheses and comma are not punctuation, they are just characters in the symbol, to make it easier for the human reader.)
    Available(T,I) means that treasure T is at its starting location at time I. For instance Available(Ruby,3) means that the ruby is available at location H at time 3.
    Has(T,I) means that the player is in possession of treasure T at time I. For example, Has(Gold,3) means that the player has the gold at time 3. 

If there is a maximum of K steps, then for each time I=0 ... K, there should be an atom At(N,I) for each node N and atoms Available(T,I) and Has(T,I) for each treasure T.

There are 12 kinds of propositions.

1. The player is only at one place at a time.
For any time I, for any two distinct nodes M and N, ~(At(M,I) ^ At(N,I)).
In CNF this becomes ~At(M,I) V ~At(N,I).
For example, ~At(C,2) V ~At(F,2).

2. If the player has treasure T at time I then T is not available at time I.
Thus Has(T,I) => ~Available(T,I).
In CNF, ~Has(T,I) V ~Available(T,I).
For example, ~Has(Gold,1) V ~Available(Gold,1).

3. The player must move on edges. Suppose that node N is connected to M1 ... Mk. For any time I, if the player is at node N at time I, then the player moves to M1 or to M2 ... at time I+1.
Thus At(N,I) => At(M1,I+1) V ... V At(Mk,I+1).
In CNF, ~At(N,I) V At(M1,I+1) V ... V At(Mk,I+1).
For example, ~At(C,2) V At(START,3) V At(D,3) V At(F,3)

Assume that there is a self-loop from GOAL to itself, so that once the player has reached GOAL he can stay there.

4. If node N has toll T and the player is at N at time I+1, then the player must have T at time I.
Thus, At(N,I+1) => Has(T,I).
In CNF ~At(N,I+1) V Has(T,Ii).
For example, ~At(B,3) V Has(Wand,2)

5. If treasure T is initially at node N and is available at time I and the player is at N at time I+1, then at time I+1 the player has T.
Thus, ~Available(T,I) V ~At(N,I+1) V Has(T,I+1).
In CNF, ~Available(T,I) V ~At(N,I+1) V Has(T,I+1).
For example, ~Available(Gold,1) ^ At(B,2) => Has(Gold,2).

6. If node N has toll T and the player is at N at time I, then the player no longer has T at time I.
Thus At(N,I) => ~Has(T,I).
In CNF, ~At(N,I) V ~Has(T,I).
For instance ~At(C,3) V ~Has(Gold,3).

7. If treasure T is available at I, and the player is at node N which is not the home of T at I+1, then T is available at I+1.
Thus Available(T,I) ^ At(N,I+1) => Available(N,I+1).
In CNF, ~Available(T,I) V ~At(N,I+1) V Available(N,I+1).
For example ~Available(Gold,2) V~ At(E,3) V Available(Gold,3).

8. If treasure T is not available at time I, then it is not available at time I+1. Thus
~Available(T,I) => ~Available(T,I+1).
In CNF, Available(T,I) V ~Available(T,I+1).
For example, Available(Gold,3) V ~Available(Gold,4).

9. If treasure T has been spend at time I, then the player does not have it at time I+1. Thus
~Available(T,I) ^ ~Has(T,I) => ~ Has(T,I+1).
In CNF, Available(T,I) V Has(T,I) V ~Has(T,I+1).

10. If the player has treasure T at time I and is at node N at time I+1, and N does not require T as a toll, then the player still has T at I+1.
Thus Has(T,I) ^ At(N,I+1) => Has(T,I+1).
In CNF ~Has(T,I) V ~At(N,I+1) V Has(T,I+1).
For example ~Has(Gold,1) ^ At(D,2) V Has(Gold,2).

11. The player is at START at time 0. At(START,0).

12. The treasures are all available at time 0. For each treasure T, Available(T,0).

13. The player is at GOAL at time K. At(GOAL,K).

[In the theory of logic-based planning, axioms such as 1 and 2, which characterize what conditions can hold at a single time, are called "domain constraints". Axioms such as 3 and 4, which characterize what states can follow one another are called "feasibility axioms" or "precondition axioms"; the form of these here is a little unusual since we aren't explicitly representing actions. Axioms such as 5, 6, which characterize what changes between successive states are called "causal axioms". Axioms such as 7, 8, 9, 10 which characterize what remains unchanged between successibe states are called "frame axioms". Axioms 11 and 12 are initial conditions and axiom 13 is a goal condition].
Specifications
Input / Output.
All three programs take their input from a text file produce their output to a text file. (If you want, you may use standard input and output.)
Davis-Putnam
The input to the Davis-Putnam procedure has the following form: An atom is denoted by a natural number: 1,2,3 ... The literal P is the same number as atom P; the literal ~P is the negative. A clause is a line of text containing the integers of the corresponding literals. After all the clauses have been given, the next line is the single value 0; anything further in the file is ignored in the execution of the procedure and reproduced at the end of the output file. (This is the mechanism we will use to allow the front end to communicate to the back end.)

The output from the Davis-Putnam procedure has the following form: First, a list of pairs of atom (a natural number) and truth value (either T or F). Second, a line containing the single value 0. Third, the back matter from the input file, reproduced.

Example: Given the input

1 2 3 
-2 3 
-3 
0 
This is a simple example with 3 clauses and 3 atoms.

Davis-Putnam will generate the output

1 T 
2 F
3 F
0 
This is a simple example with 3 clauses and 3 atoms.

This corresponds to the clauses

P V Q V R. 
~Q V R. 
~R. 

If the clauses have no solution, then Davis-Putnam outputs a single line containing a 0, followed by the back-matter in the input file.

Note: Your implementation of Davis-Putnam must work on any set of clauses, not just those that are generated by the Maze program.

You may assume that there are no more than 1000 atoms and no more than 10,000 clauses.
Front end
The front end takes as input a specification of a maze and a problem and generates as output a set of clauses to be satisfied.

The format of the input contains the following elements:

    First line: A list of the nodes, separated by white space. Each node is a string of up to five characters.
    Second line: A list of the treasures, separated by white space. Each treasure is a string of up to ten characters.
    Third line; The number of allowed steps.
    Remaining lines: The encoding of the maze. Each line consists of the following parts
        A node N.
        The keyword "TREASURES" followed by the list of treasures, separated by white space.
        The keyword "TOLLS" followed by the list of tolls, separated by white space.
        The keyword "NEXT" followed by the list of nodes that N is connected to, separated by white space. 

Thus, the above maze, with a maximum of 5 steps,

START A B C D E F G  GOAL
GOLD WAND RUBY 
5
START TREASURES TOLLS NEXT A C D
A TREASURES TOLLS NEXT START B D
B TREASURES GOLD TOLLS WAND NEXT A E
C TREASURES TOLLS GOLD NEXT START D GOAL
D TREASURES WAND TOLLS NEXT START A C E F
E TREASURES TOLLS NEXT B D F G
F TREASURES TOLLS RUBY NEXT D E G GOAL
G TREASURES RUBY TOLLS WAND NEXT E F
GOAL TREASURES TOLLS NEXT C F GOAL

You may assume that the input is correctly formatted. You do not have to do any error checking on the input. The output consists of

    1. A set of clauses suitable for inputting to Davis-Putnam as described above. Note that these constraints are already in clausal form (CNF) and therefore you do not have to implement a program to translate arbitrary Boolean formulas to CNF.
    2. A key to allow the back end to translate the numbers used for propositional atoms in the clauses into the correct path. The format of this is up to you. My suggestion would be that, for each atom At(N,I), you have a line of the form "proposition-number N I" 

Back-end
The back end takes as input the output that Davis-Putnam generates when run on the output of the front end. It generates as output the path that solves the problem. If the input indicates that the clauses have no solution, the back end should output the message "NO SOLUTION".
Another example for Davis-Putnam
The following is the input-output pair just for the Davis-Putnam module --- not the front and back ends --- corresponding to the example in the class notes.

    Input for Davis-Putnam
    Output from Davis-Putnam 

A Simpler Maze Example
It would probably be a mistake to begin your testing using the above example, with its 90 propositional atoms and hundreds of propositions. Rather, I would suggest that you start by working on the following maze with a search limit of 4.

Obvious Optimizations for Front End
There are a couple of easy optimizations that can be made in the front end which eliminate a substantal number of the atoms. For example, you can eliminate all the nodes that are not reachable from START in K steps (or, even better, eliminate all the atoms At(N,I) where node N is not reachable from START in I steps.) Whether you want to implement these is up to you; it is quite likely that they will make the system run noticeably faster.

Deliverable
You should submit by email (a) the source code; (b) instructions for running it, if there's anything at all non-obvious about it. Nothing else.
Grading
The Davis-Putnam program is worth 60% of the grade; the front end is worth 35%; the back end is worth 5%. In each of these, a program that does not compile will get a maximum of 10%; a correct program will get 90%; the remaining 10% is for being well-written and well commented. 