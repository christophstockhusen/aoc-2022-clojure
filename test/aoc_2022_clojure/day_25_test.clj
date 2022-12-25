(ns aoc-2022-clojure.day-25-test 
  (:require [aoc-2022-clojure.day-25 :refer [a]]
            [clojure.test :refer [deftest is testing]]))

(def input "1=-0-2
12111
2=0=
21
2=01
111
20012
112
1=-1=
1-12
12
1=
122")

(deftest full-oh-hot-air-a-test
  (testing "Day 25 - Part 1"
    (is (= "2=-1=0" (a input)))))