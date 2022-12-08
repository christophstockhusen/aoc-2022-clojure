(ns aoc-2022-clojure.day-08-test
  (:require [aoc-2022-clojure.day-08 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "30373
25512
65332
33549
35390
")

(deftest treetop-tree-house-a-test
  (testing "Day 8 - Part 1"
    (is (= 21 (a input)))))

(deftest treetop-tree-house-b-test
  (testing "Day 8 - Part 2"
    (is (= 8 (b input)))))
