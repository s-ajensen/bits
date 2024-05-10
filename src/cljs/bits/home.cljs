(ns bits.home
  (:require [bits.page :as page]
            [c3kit.apron.corec :as ccc]
            [c3kit.wire.js :as wjs]
            [clojure.string :as str]
            [reagent.core :as reagent]))

(defn num-field [])

(def default "0")

(defn home []
  (let [num (reagent/atom default)]
    (fn []
      [:div.homepage-container
       [:h1 "Bits - The Simple Base Converter"]
       [:div
        [:label {:for "binary"} "Binary "]
        [:input.binary {:type      "text"
                        :name      "binary"
                        :value     @num
                        :style     {:text-align "right"}
                        :on-change (fn [e]
                                     (let [val (wjs/e-text e)]
                                       (when (str/blank? (str/replace-all val #"1|0" ""))
                                         (reset! num val))))}]]
       [:div
        [:label {:for "decimal"} "Decimal "]
        [:input.decimal {:type      "text"
                         :name      "decimal"
                         :value     (js/parseInt @num 2)
                         :style     {:text-align "right"}
                         :on-change (fn [e]
                                      (let [val (wjs/e-text e)]
                                        (when (or (= "0" val)
                                                  (and (str/blank? (str/replace-all val #"[0-9]" ""))
                                                       (not (str/starts-with? val "0"))))
                                          (reset! num (js-invoke (js/parseInt val) "toString" 2)))))}]]])))

(defmethod page/render :home [_]
  [home])