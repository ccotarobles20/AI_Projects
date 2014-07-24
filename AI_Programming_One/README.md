 Programming Assignment 1: CYK Parser

Assigned: Jan. 30
Due: Feb. 13
General Description
Implement the CYK Parser (see Russell and Norvig, pp. 893-895 and handouts) for the context-free grammar and lexicon showed below:
Grammar

    S -> Noun Verb [0.2]
    S -> Noun VerbAndObject [0.3]
    S -> Noun VPWithPPList [0.1]
    S -> NP Verb [0.2]
    S -> NP VerbAndObject [0.1]
    S -> NP VPWithPPList [0.1]
    NP -> Noun PP [0.8]
    NP -> Noun PPList [0.2]
    PP -> Prep Noun [0.6]
    PP -> Prep NP [0.4]
    PPList -> PP PP [0.6]
    PPList -> PP PPList [0.4]
    VerbAndObject -> Verb Noun [0.5]
    VerbAndObject -> Verb NP [0.5]
    VPWithPPList -> Verb PP [0.3]
    VPWithPPList -> Verb PPList [0.1]
    VPWithPPList -> VerbAndObject PP [0.4]
    VPWithPPList -> VerbAndObject PPList [0.2]

    Noun -> amy [0.1]
    Noun -> dinner [0.2]
    Noun -> fish [0.2]
    Noun -> streams [0.1]
    Noun -> swims [0.2]
    Noun -> tuesday [0.2]
    Prep -> for [0.5]
    Prep -> in [0.3]
    Prep -> on [0.2]
    Verb -> ate [0.7]
    Verb -> streams [0.1]
    Verb -> swim [0.2]

The program should read a single sentence and print out the most probable parse tree with its probability. For instance given the input "Fish swim in streams" the program should print out

S 
   Noun fish
   VPWithPPList
      Verb  swim
      PP
         Prep in
         Noun streams
Probability = 0.0000216 (2.16e-5).

If the input sentence cannot be parsed as a sentence e.g. "toy in" then the program should print out "This sentence cannot be parsed".

You may assume that the sentence is correctly formatted; that is, it consists of words in the lexicon in lower case separated by spaces.
Extra credit (2 points)
Print out both the most likely parse and the second most likely parse.

Hint: The second most likely parse contains exactly one second-best decision; all the other decisions are first-best. But that doesn't mean that it differs from the most likely parse in only one node, because a decision at a top node can affect the decisions further down.

Note: This is quite tricky -- certainly more thought than the main part of the assignment. Do not attempt this unless (a) you have finished the main part; (b) you have spare time; (c) you did well in Basic Algorithms.
Comments about implementation

You may assume that the sentence is on a single line of input, unpunctuated, and contains only words in the grammar. Write the program to be case insensitive; that is, convert everything to lower case and then proceed.

You may use C, C++, or Java. You may not use Python, because the Python code is on the course web site. if you want to use another language, check with me. You may input the test sentences in any reasonable manner, except imbedding them in the code.
To submit
Submit at the NYU Classes site.

    1. Source code for your program.
    2. A README file explaining how to run the code. If you have done the extra credit part, that should be stated here.
    3. Any other files needed to run the code (e.g. if you decide to put the grammar or lexicon in an external file)
    4. The output of the program for the following inputs:
        A. Fish swim in streams
        B. Fish in streams swim
        C. Amy ate fish for dinner
        D. Amy ate fish for dinner on Tuesday
        E. Amy ate for (Should output "This sentence cannot be parsed"). 
