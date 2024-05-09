(ns bits.repl
  (:require
   [bits.init :as init]
   [bits.main :as main]))

(println "Welcome to the bits REPL!")
(println "Initializing")
(init/install-legend!)
(main/start-db)
(require '[c3kit.bucket.api :as db])
