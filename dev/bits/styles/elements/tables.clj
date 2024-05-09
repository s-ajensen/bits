(ns bits.styles.elements.tables
  (:refer-clojure :exclude [rem])
  (:require [bits.styles.core :refer :all]))

(def screen
(list

[:table {
  :border [[(px 1) "solid" light-grey]]
  :border-radius border-radius
  :padding size-0
  :width "100%"
}]

[:th :td {
  :padding size-minus-1
  :text-align "left"
}]

))
