(ns bits.room
  (:require [bits.core :as cc]
            [bits.page :as page]))

(defmethod page/render :room [_]
  [:h1 (str "hello!" @page/state)])