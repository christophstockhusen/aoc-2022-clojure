(ns aoc-2022-clojure.day-04-test 
  (:require [aoc-2022-clojure.day-04 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8")

(deftest XXX-a-test
  (testing "Day 04 - Part 1"
    (is (= 2 (a input)))))

(deftest XXX-b-test
  (testing "Day 04 - Part 2"
    (is (= 4 (b input)))))
