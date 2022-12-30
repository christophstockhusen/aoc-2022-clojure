(ns aoc-2022-clojure.day-24-test 
  (:require [aoc-2022-clojure.day-24 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "#.#####
#.....#
#>....#
#.....#
#...v.#
#.....#
#####.#")

(deftest blizzard-basin-a-test
  (testing "Day 24 - Part 1"
    (is (= 18 (a input)))))

(deftest blizzard-basin-b-test
  (testing "Day 24 - Part 2"
    (is (= 54 (b input)))))