;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; This Source Code Form is "Incompatible With Secondary Licenses", as
;; defined by the Mozilla Public License, v. 2.0.
;;
;; Copyright (c) 2020 UXBOX Labs SL

(ns app.main.ui.workspace.shapes.svg-raw
  (:require
   #_[app.main.data.workspace :as dw]
   [app.main.refs :as refs]
   #_[app.main.store :as st]
   #_[app.main.streams :as ms]
   [app.main.ui.shapes.svg-raw :as svg-raw]
   [app.main.ui.shapes.shape :refer [shape-container]]
   [app.main.ui.workspace.effects :as we]
   #_[app.util.dom :as dom]
   [rumext.alpha :as mf]
   #_[app.common.geom.shapes :as gsh]
   #_[app.util.debug :refer [debug?]]
   ))

(defn- svg-raw-wrapper-factory-equals?
  [np op]
  (let [n-shape (unchecked-get np "shape")
        o-shape (unchecked-get op "shape")
        n-frame (unchecked-get np "frame")
        o-frame (unchecked-get op "frame")]
    (and (= n-frame o-frame)
         (= n-shape o-shape))))

(defn svg-raw-wrapper-factory
  [shape-wrapper]
  (let [svg-raw-shape (svg-raw/svg-raw-shape shape-wrapper)]
    (mf/fnc svg-raw-wrapper
      {::mf/wrap [#(mf/memo' % svg-raw-wrapper-factory-equals?)]
       ::mf/wrap-props false}
      [props]
      (let [shape (unchecked-get props "shape")
            frame (unchecked-get props "frame")

            {:keys [id x y width height]} shape

            childs-ref (mf/use-memo (mf/deps shape) #(refs/objects-by-id (:shapes shape)))
            childs     (mf/deref childs-ref)

            handle-mouse-down (we/use-mouse-down shape)
            handle-context-menu (we/use-context-menu shape)
            handle-pointer-enter (we/use-pointer-enter shape)
            handle-pointer-leave (we/use-pointer-leave shape)]

        [:& svg-raw-shape {:frame frame
                           :shape shape
                           :childs childs}]
        #_[:> shape-container {:shape shape
                             :on-mouse-down handle-mouse-down
                             :on-context-menu handle-context-menu
                             :on-pointer-over handle-pointer-enter
                             :on-pointer-out handle-pointer-leave}
         [:& svg-raw-shape
          {:frame frame
           :shape shape
           :childs childs}]]))))

