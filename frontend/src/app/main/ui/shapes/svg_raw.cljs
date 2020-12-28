;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; This Source Code Form is "Incompatible With Secondary Licenses", as
;; defined by the Mozilla Public License, v. 2.0.
;;
;; Copyright (c) 2020 UXBOX Labs SL

(ns app.main.ui.shapes.svg-raw
  (:require
   [rumext.alpha :as mf]
   #_[app.config :as cfg]
   [app.common.geom.shapes :as gsh]
   #_[app.main.ui.shapes.attrs :as attrs]
   [app.util.object :as obj]
   #_[app.main.ui.context :as muc]
   #_[app.main.data.fetch :as df]
   #_[promesa.core :as p]))


(defn svg-raw-shape [shape-wrapper]
  (mf/fnc svg-raw-shape
    {::mf/wrap-props false}
    [props]
    (let [frame  (unchecked-get props "frame")
          shape  (unchecked-get props "shape")
          childs (unchecked-get props "childs")

          {:keys [tag attrs] :as content} (:content shape)]
      (.log js/console "??" tag (clj->js (:attrs content)))

      (cond
        (map? content)
        (let [transform (gsh/transform-matrix shape)
              attrs (cond-> (clj->js attrs)
                      (= tag :svg)
                      (-> (obj/set! "data-caca" "caca")
                          (obj/set! "x" (:x shape))
                          (obj/set! "y" (:y shape))
                          (obj/set! "width" (:width shape))
                          (obj/set! "height" (:height shape))
                          (obj/set! "transform" transform)))]
          [:> (name tag) attrs
           (for [item childs]
             [:& shape-wrapper {:frame frame
                                :shape item
                                :key (:id item)}])])

        (string? content) content
        ;; :else (first content)
        :else "??"
        ))))


#_(defn svg-raw-shape
  [shape-wrapper]
  (mf/fnc svg-raw-shape
    {::mf/wrap-props false}
    [props]
    (let [frame          (unchecked-get props "frame")
          shape          (unchecked-get props "shape")
          childs         (unchecked-get props "childs")

          {:keys [tag attrs] :as content}        (:content shape)]

      (cond
        (map? content)
        [:> (:tag content)
         (clj->js (:attrs content))
         #_(for [item childs]
           [:& shape-wrapper {:frame frame
                              :shape item
                              :key (:id item)}])]

        :else (first content)))))
