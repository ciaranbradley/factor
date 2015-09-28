(ns factor.core
    (:require [reagent.core :as reagent :refer [atom]]))

;; -----------------------------------------
;; Logic

(defn largest [num1 num2]
  (if (>= num1 num2) num1 num2))

(defn smallest [num1 num2]
  (if (< num1 num2) num1 num2))

(defn is-factor? [number divisor]
  (if (= 0 (mod number divisor)) true false))

(defn calculate-factors [number]
  (sort-by first  < (into #{}
        (filter identity
                (map (fn [x]
                       (if (is-factor? number x)
                         [(smallest x (/ number x))
                          (largest x (/ number x))]))
                     (range 1 (.ceil js/Math (/ number 2))))))))

;; -------------------------
;; Views

(defn factor-input [value]
  [:input {:type "text"
           :value @value
           :on-change #(reset! value (-> % .-target .-value))}])

(defn factor-table [number]
  [:table
   [:thead
   [:tr
    [:td "First"]
    [:td "Second"]
    [:td "Difference"]]]
   [:tbody
    (for [[first second :as item] (calculate-factors number)]
      ^{:key item} [:tr
                    [:td first]
                    [:td second]
                    [:td (.abs js/Math (- second first))]])]])

(defn factoriser []
  (let [val (reagent/atom "10")]
    (fn []
      [:div
       [:p "Number to be factorised: " [factor-input val]]
       [factor-table @val]])))

(defn home-page []
  [:div [:h2 "Factoriser"]
   [factoriser]])

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [factoriser] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
