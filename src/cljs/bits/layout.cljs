(ns bits.layout
  (:require [bits.page :as page]
            [c3kit.wire.ajax :as ajax]
            [c3kit.wire.flash :as flash]
            [bits.home]
            [bits.room]))

(defn default []
  [:div#content
   [flash/flash-root]
   (when (ajax/activity?) [:div.site-spinner])
   [:header
    [:div.container.horizontal-inset-plus-1.inliner.space-between]]
   (page/render @page/current)])