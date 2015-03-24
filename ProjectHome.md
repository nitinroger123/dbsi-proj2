Let S denote the set of basic terms, and let k
be the cardinality of S. Create an array A[.md](.md) of size 2k indexed by the subsets of S. The
array elements are records containing: The number n of basic terms in the corresponding
subset; the product p of the selectivities of all terms in the subset; a bit b determining
whether the no-branch optimization was used to get the best cost, initialized to 0; the
current best cost c for the subset; the left child L and right child R of the subplans giving
the best cost. L and R range over indexes for A[.md](.md), and are initialized to ∅