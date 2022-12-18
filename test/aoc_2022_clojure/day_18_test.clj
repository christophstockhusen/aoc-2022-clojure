(ns aoc-2022-clojure.day-18-test 
  (:require [aoc-2022-clojure.day-18 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5")

(deftest boiling-boulders-a-test
  (testing "Day 18 - Part 1"
    (is (= 64 (a input)))))

(deftest boiling-boulders-b-test
  (testing "Day 18 - Part 2"
    (is (= 58 (b input)))))