(ns bits.home
  (:require [bits.page :as page]
            [c3kit.apron.corec :as ccc]
            [c3kit.wire.js :as wjs]
            [clojure.string :as str]
            [reagent.core :as reagent]))

(defn num-field [num-ratom name in-fn out-fn valid?-fn]
  (let [label (str (str/upper-case (subs name 0 1))
                   (subs name 1)
                   " ")]
    [:div
     [:label {:id (str name "-label") :for name} label]
     [:input {:id        name
              :type      "text"
              :name      name
              :value     (when-not (str/blank? @num-ratom)
                           (in-fn @num-ratom))
              :style     {:text-align "right"}
              :on-change (fn [e]
                           (let [val (wjs/e-text e)]
                             (when (valid?-fn val)
                               (reset! num-ratom (out-fn val)))))}]]))

(def default "0")
(def num (reagent/atom default))

(defn valid-binary? [val]
  (str/blank? (str/replace-all val #"1|0" "")))

(defn binary->decimal [s]
  (js/parseInt s 2))
(defn decimal->binary [s]
  (when-not (str/blank? s)
    (js-invoke (js/parseInt s) "toString" 2)))
(defn valid-decimal? [s]
  (or (= "0" s)
      (and (str/blank? (str/replace-all s #"[0-9]" ""))
           (not (str/starts-with? s "0")))))

(defn home []
  (fn []
    [:div.homepage-container
     [:h1 "Bits - The Simple Base Converter"]
     [num-field num "binary" identity identity valid-binary?]
     [num-field num "decimal" binary->decimal decimal->binary valid-decimal?]
     ]))

(defmethod page/render :home [_]
  [home])