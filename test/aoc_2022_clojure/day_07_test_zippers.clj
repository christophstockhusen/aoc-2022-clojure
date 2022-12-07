(ns aoc-2022-clojure.day-07-test-zippers 
  (:require [aoc-2022-clojure.day-07-zippers :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k")

(deftest no-space-left-on-device-a-test
  (testing "Day 7 - Part 1 (using zippers)"
    (is (= 95437 (a input)))))

(deftest no-space-left-on-device-b-test
  (testing "Day 7 - Part 2 (using zippers)"
    (is (= 24933642 (b input)))))