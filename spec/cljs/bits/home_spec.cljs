(ns bits.home-spec
  (:require-macros [c3kit.wire.spec-helperc :refer [should-not-select should-select]]
                   [speclj.core :refer [before describe context it should= should-contain with-stubs]])
  (:require [bits.home :as sut]
            [bits.layout :as layout]
            [bits.page :as page]
            [c3kit.wire.spec-helper :as wire-helper]
            [c3kit.wire.spec-helper :as wire]
            [speclj.core]
            ))

(defn mock-parse [s]
  (str s "-parsed"))
(defn mock-valid? [s]
  (> 2 (count s)))

(describe "Home page"

  (wire-helper/with-root-dom)

  (before (page/install-page! :home)
          (wire/render [layout/default]))

  (context "number field"

    (before (wire/render [sut/num-field])))

  (context "number fields"

    (context "binary"

      (it "default value"
        (should= "0" (wire/value "input.binary")))

      (it "inputs 0 & 1"
        (wire/change! "input.binary" "0110")
        (should= "0110" (wire/value "input.binary")))

      (it "only allows 0 & 1"
        (wire/change! "input.binary" "02")
        (should= "0" (wire/value "input.binary")))

      (it "updates others"
        (wire/change! "input.binary" "11")
        (should= "11" (wire/value "input.binary"))
        (should= "3" (wire/value "input.decimal"))))

    (context "decimal"

      (it "default value"
        (should= "0" (wire/value "input.decimal")))

      (it "inputs 0 - 9"
        (wire/change! "input.decimal" "9876543210")
        (should= "9876543210" (wire/value "input.decimal")))

      (it "disallows leading 0's"
        (wire/change! "input.decimal" "01")
        (should= "0" (wire/value "input.decimal")))

      (it "only allows 0 - 9"
        (wire/change! "input.decimal" "asdf")
        (should= "0" (wire/value "input.decimal")))

      (it "allows 0"
        (wire/change! "input.decimal" "")
        (wire/change! "input.decimal" "0")
        (should= "0" (wire/value "input.decimal"))))))
