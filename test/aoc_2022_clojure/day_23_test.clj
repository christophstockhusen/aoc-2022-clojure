(ns aoc-2022-clojure.day-23-test 
  (:require [aoc-2022-clojure.day-23 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "....#..
..###.#
#...#.#
.#...##
#.###..
##.#.##
.#..#..")

(deftest unstable-diffusion-a-test
  (testing "Day 23 - Part 1"
    (is (= 110 (a input)))))

(deftest unstable-diffusion-b-test
  (testing "Day 23 - Part 2"
    (is (= 20 (b input)))))
