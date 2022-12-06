(ns aoc-2022-clojure.day-06-test 
  (:require [aoc-2022-clojure.day-06 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input-a "mjqjpqmgbljsphdztnvjfqwrcgsmlb")

(deftest tuning-trouble-a-test
  (testing "Day 06 - Part 1"
    (is (= 7 (a input-a)))))

(def input-b "mjqjpqmgbljsphdztnvjfqwrcgsmlb")

(deftest tuning-trouble-b-test
  (testing "Day 06 - Part 2"
    (is (= 19 (b input-b)))))
