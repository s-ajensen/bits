(ns bits.home
  (:require [bits.page :as page]
            [c3kit.apron.corec :as ccc]
            [c3kit.wire.js :as wjs]
            [clojure.string :as str]
            [reagent.core :as reagent]))

(defn num-field [num-ratom name from-fn to-fn valid?-fn]
  (let [label (str (str/upper-case (subs name 0 1))
                   (subs name 1)
                   " ")]
    [:div
     [:label {:id (str name "-label") :for name} label]
     [:input {:id        name
              :type      "text"
              :name      name
              :value     (when-not (str/blank? @num-ratom)
                           (from-fn @num-ratom))
              :style     {:text-align "right"}
              :on-change (fn [e]
                           (let [val (wjs/e-text e)]
                             (when (valid?-fn val)
                               (reset! num-ratom (to-fn val)))))}]]))

(def default "0")
(def num (reagent/atom default))

(defn present-binary [s]
  (->> (str/reverse s)
       (partition-all 4)
       (map (partial apply str))
       (str/join " ")
       (str/reverse)))
(defn coerce-binary [s]
  (str/replace s " " ""))
(defn valid-binary? [s]
  (str/blank? (str/replace-all s #"1|0" "")))

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
     [num-field num "binary" present-binary coerce-binary valid-binary?]
     [num-field num "decimal" binary->decimal decimal->binary valid-decimal?]
     ]))

(defmethod page/render :home [_]
  [home])