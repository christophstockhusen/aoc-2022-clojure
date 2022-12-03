(ns aoc-2022-clojure.day-03-test 
  (:require [aoc-2022-clojure.day-03 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw")

(deftest rucksack-reorganization-a-test
  (testing "Day 03 - Part 1"
    (is (= 157 (a input)))))

(deftest rucksack-reorganization-b-test
  (testing "Day 03 - Part 2"
    (is (= 70 (b input)))))
