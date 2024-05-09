(ns bits.styles.components.menus
  (:refer-clojure :exclude [rem])
  (:require [bits.styles.core :refer :all]))

(def screen
(list

[:.homepage-container
 {:display "flex"
  :flex-direction "column"
  :align-items "center"
  :justify-content "center"
  :height "100vh"}]

[:.nickname-input :.room-actions
 {:margin-bottom "15px"}]

[:.room-actions
 {:display "flex"
  :justify-content "center"
  :gap "10px"}]

))
