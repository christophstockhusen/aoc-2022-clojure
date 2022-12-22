(ns aoc-2022-clojure.day-21-test 
  (:require [aoc-2022-clojure.day-21 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "root: pppw + sjmn
dbpl: 5
cczh: sllz + lgvd
zczc: 2
ptdq: humn - dvpt
dvpt: 3
lfqf: 4
humn: 5
ljgn: 2
sjmn: drzm * dbpl
sllz: 4
pppw: cczh / lfqf
lgvd: ljgn * ptdq
drzm: hmdt - zczc
hmdt: 32")

(deftest monkey-math-a-test
  (testing "Day 21 - Part 1"
    (is (= 152 (a input)))))

(deftest monkey-math-b-test
  (testing "Day 21 - Part 2"
    (is (= 301 (b input)))))
