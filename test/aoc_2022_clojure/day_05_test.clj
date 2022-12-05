(ns aoc-2022-clojure.day-05-test 
  (:require [aoc-2022-clojure.day-05 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2")

(deftest supply-stacks-a-test
  (testing "Day 05 - Part 1"
    (is (= "CMZ" (a input)))))

(deftest supply-stacks-b-test
  (testing "Day 05 - Part 2"
    (is (= "MCD" (b input)))))