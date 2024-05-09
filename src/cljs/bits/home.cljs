(ns bits.home
  (:require [bits.page :as page]))

(defmethod page/render :home [_]
  [:div.homepage-container
   [:h1 "Bits - The Simple Base Converter"]
   ])