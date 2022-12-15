(ns aoc-2022-clojure.day-15 
  (:require [clojure.java.io :as io]
            [clojure.math :as math]))

(defn parse-input [input]
  (->> (re-seq #"-?\d+" input)
       (map parse-long)
       (partition 4)
       (map (partial partition 2))
       (map vec)))

(defn manhatten-distance [[x1 y1] [x2 y2]]
  (reduce + (map #(abs (- %1 %2)) [x1 y1] [x2 y2])))

(defn radius-of-sensor [[sensor-coordinates beacon-coordinates]]
  (manhatten-distance sensor-coordinates beacon-coordinates))

(defn intersection-with-line [[sensor-coordinates beacon-coordinates] line-no]
  (let [radius (radius-of-sensor [sensor-coordinates beacon-coordinates])
        distance-to-line (abs (- (second sensor-coordinates) line-no))
        half-length (- radius distance-to-line)]
    (if (pos? half-length)
      [(- (first sensor-coordinates) half-length)
       (+ (first sensor-coordinates) half-length)]
      nil)))

(defn covered-intervals-in-line [sensors line-no]
  (filter some? (map #(intersection-with-line % line-no) sensors)))

(defn merge-two-intervals [[a b] [c d]]
  (if (<= c b) 
    [[a (max b d)]]
    [[a b] [c d]]))

(defn merge-intervals [intervals]
  (let [intervals (sort-by first intervals)]
    (if (<= (count intervals) 1)
      intervals
      (reduce
       (fn [merged interval]
         (let [recent (peek merged)
               new-intervals (merge-two-intervals recent interval)]
           (into (pop merged) new-intervals)))
       [(first intervals)] (rest intervals)))))

(defn beacons-in-line [sensors line-no]
  (->> (map second sensors)
       (filter #(= line-no (second %)))
       (map first)
       (distinct)))

(defn interval-length [[a b]]
  (cond-> (abs (- a b))
    (not= (math/signum a)
          (math/signum b)) inc))

(defn free-positions-in-line [sensors line-no]
  (let [intersecting-intervals (covered-intervals-in-line sensors line-no)
        merged-intervals (merge-intervals intersecting-intervals)
        beacons (beacons-in-line sensors line-no)]
    (- (reduce + (map interval-length merged-intervals))
       (count beacons))))

(defn a
  ([] (a (slurp (io/resource "15.txt")) 2000000))
  ([input row] (free-positions-in-line (parse-input input) row)))

(defn border-lines
  ;; Returns the b of line ax + x = b for all four lines
  ;; just one step outside of the given sensor's range.
  ;; First two are of the lines with slope 1,
  ;; second two are of the lines with slope -1.
  [sensor]
  (let [[x y] (first sensor)
        radius (radius-of-sensor sensor)]
    [[(inc (+ (- y x) radius))
      (dec (- (- y x) radius))]
     [(inc (+ (+ x y) radius))
      (dec (- (+ x y) radius))]]))

(defn intersection-point [b1 b2]
  [(/ (- b2 b1) 2) (/ (+ b1 b2) 2)])

(defn covered? [sensors [x y]]
  (reduce #(or %1 %2) 
          (map (fn [[[sx sy] [bx by]]]
                 (<= (manhatten-distance [sx sy] [x y])
                     (radius-of-sensor [[sx sy] [bx by]]))) sensors)))

(defn distress-beacon-position [sensors]
  (let [borders (map border-lines sensors)
        increasing (mapcat first borders)
        decreasing (mapcat second borders)
        intersections (for [i increasing
                            d decreasing]
                        (intersection-point i d))
        intersections (distinct (filter (fn [p] (every? #(<= 0 % 4000000) p))
                                        intersections))
        uncovered (filter #(not (covered? sensors %)) intersections)]
    (first uncovered)))

(defn tuning-frequency [[x y]]
  (+ (* 4000000 x) y))

(defn b
  ([] (b (slurp (io/resource "15.txt"))))
  ([input]  (->> (parse-input input)
                 (distress-beacon-position)
                 (tuning-frequency))))
