(ns aoc-2022-clojure.day-01-test 
  (:require [aoc-2022-clojure.day-01 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "1000
2000
3000

4000

5000
6000

7000
8000
9000

10000")

(deftest calorie-counting-a-test
      (testing "Day 01 - Part 1"
        (is (= 24000 (a input))))) 

(deftest calorie-counting-b-test
      (testing "Day 01 - Part 1"
        (is (= 45000 (b input))))) 