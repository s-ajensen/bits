(ns bits.main
  (:require [accountant.core :as accountant]
            [c3kit.apron.log :as log]
            [c3kit.apron.utilc :as utilc]
            [c3kit.wire.ajax :as ajax]
            [c3kit.wire.api :as api]
            [c3kit.wire.flash :as flash]
            [c3kit.wire.js :as wjs]
            [bits.config :as config]
            [bits.layout :as layout]
            [bits.routes :as router]
            [reagent.dom :as dom]
            ))

;; MDM: Needed with advanced compilation so pages can load content
(goog/exportSymbol "goog.require" goog/require)

(defn load-config [{:keys [api-version anti-forgery-token] :as config}]
  (api/configure! {:version      api-version
                   :ajax-prep-fn (ajax/prep-csrf "X-CSRF-Token" anti-forgery-token)
                   })
  (config/install! config)
  (if @config/production?
    (log/warn!)
    (log/debug!)))

(defn dispatch-and-render []
  (router/app-routes)
  (accountant/dispatch-current!)
  (dom/render [layout/default] (wjs/element-by-id "app-root")))

(defn establish-session [config]
  (config/install! config))

(defn ^:export main [payload-src]
  (let [{:keys [config user flash]} (utilc/<-transit payload-src)]
    (load-config config)
    (run! flash/add! flash)
    (dispatch-and-render)
    (establish-session {})))

(enable-console-print!)