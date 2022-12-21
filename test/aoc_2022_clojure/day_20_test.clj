(ns aoc-2022-clojure.day-20-test 
  (:require [clojure.test :refer [deftest is testing]]))

(def input "1
2
-3
3
-2
0
4")

(deftest grove-positioning-system-a-test
  (testing "Day 20 - Part 1"
    (is (= 3 (a input)))))

(deftest grove-positioning-system-b-test
  (testing "Day 20 - Part 2"
    (is (= 1623178306 (a input)))))
