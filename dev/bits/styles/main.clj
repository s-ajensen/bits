(ns bits.styles.main
  (:refer-clojure :exclude [rem])
  (:require [garden.def :as garden]
            [bits.styles.core :as core]
            [bits.styles.components.menus :as menus]
            [bits.styles.elements.forms :as forms]
            [bits.styles.elements.lists :as lists]
            [bits.styles.elements.media :as media]
            [bits.styles.elements.tables :as tables]
            [bits.styles.elements.typography :as typography]
            [bits.styles.layout.document :as document]
            [bits.styles.layout.mini-classes :as mini-classes]
            [bits.styles.layout.page :as page]
            [bits.styles.layout.reset :as reset]
            [bits.styles.layout.structure :as structure]
            [bits.styles.media.responsive :as responsive]
            [bits.styles.pages.authentication :as authentication]
            [bits.styles.pages.authentication :as authentication]
            ))

(garden/defstyles screen

; Layout
;reset/screen
document/screen
page/screen
;structure/screen
mini-classes/screen

; Elements
;typography/screen
;forms/screen
;lists/screen
;media/screen
;tables/screen

; Componenents
menus/screen

; Pages
;authentication/screen

; Media
;responsive/screen

; Fonts
;core/fonts

)
