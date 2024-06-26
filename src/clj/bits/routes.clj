(ns bits.routes
  (:require [bits.config :as config]
            [c3kit.apron.corec :as ccc]
            [c3kit.apron.util :as util]
            [c3kit.wire.ajax :as ajax]
            [clojure.string :as str]
            [compojure.core :as compojure :refer [defroutes routes]]
            [ring.util.response :as response]))

(defn wrap-prefix [handler prefix not-found-handler]
  (fn [request]
    (let [path (or (:path-info request) (:uri request))]
      (when (str/starts-with? path prefix)
        (let [request (assoc request :path-info (subs path (count prefix)))]
          (if-let [response (handler request)]
            response
            (not-found-handler request)))))))

(def resolve-handler
  (if config/development?
    (fn [handler-sym] (util/resolve-var handler-sym))
    (memoize (fn [handler-sym] (util/resolve-var handler-sym)))))

(defn lazy-handle
  "Reduces load burden of this ns, which is useful in development.
  Runtime errors will occur for missing handlers, but all the routes should be tested in routes_spec.
  Assumes all handlers take one parameter, request."
  [handler-sym request]
  (let [handler (resolve-handler handler-sym)]
    (handler request)))

(defmacro lazy-routes
  "Creates compojure route for each entry where the handler is lazily loaded.
  Why are params a hash-map instead of & args? -> Intellij nicely formats hash-maps as tables :-)"
  [table]
  `(routes
     ~@(for [[[path method] handler-sym] table]
         (let [method (if (= :any method) nil method)]
           (compojure/compile-route method path 'req `((lazy-handle '~handler-sym ~'req)))))))

(defn redirect-handler [path]
  (let [segments (str/split path #"/")
        segments (map #(if (str/starts-with? % ":") (keyword (subs % 1)) %) segments)]
    (fn [request]
      (let [params   (:params request)
            segments (map #(if (keyword? %) (get params %) %) segments)
            dest     (str/join "/" segments)]
        (response/redirect dest)))))

(defmacro redirect-routes [table]
  `(routes
     ~@(for [[[path method] dest] table]
         (let [method (if (= :any method) nil method)]
           (compojure/compile-route method path 'req `((redirect-handler ~dest)))))))

(def ws-handlers
  {
   :user/fetch-data 'bits.auth/ws-fetch-user-data
   })

(defn sleep-for-10 [] (Thread/sleep 10000))
(defn spinner [_]
  (sleep-for-10)
  (ajax/ok {} nil))

(def web-routes-handlers
  (lazy-routes
    {
     ["/" :get]                                 bits.layouts/web-rich-client
     }))

(def dev-handler
  (lazy-routes
    {
     ;["/sandbox" :get]                 bits.sandbox.core/index
     ;["/sandbox/" :get]                bits.sandbox.core/index
     ;["/sandbox/:page" :get]           bits.sandbox.core/handler
     ;["/sandbox/:page/:ns" :get]       bits.sandbox.core/handler
     ;["/sandbox/:page/:ns1/:ns2" :get] bits.sandbox.core/handler
     }))

(defroutes handler
  web-routes-handlers
  (if config/production? ccc/noop dev-handler)
  )