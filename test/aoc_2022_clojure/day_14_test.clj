(ns aoc-2022-clojure.day-14-test 
  (:require [aoc-2022-clojure.day-14 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9")

(deftest regolith-reservoir-a-test
  (testing "Day 14 - Part 1"
    (is (= 24 (a input)))))

(deftest regolith-reservoir-b-test
  (testing "Day 14 - Part 2"
    (is (= 93 (b input)))))