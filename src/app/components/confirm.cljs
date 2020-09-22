(ns app.components.confirm
  (:require [reagent.core :as r]
            [react-dom]
            [clojure.core.async :refer [chan put!]]
            [oops.core :refer [ocall]]))

(def ^:private is-confirm-open?_ (r/atom false))
(def ^:private confirm-msg_ (r/atom ""))
(def ^:private confirm-ch_ (atom nil))

(defn- open-confirm [msg]
  (reset! confirm-msg_ msg)
  (reset! is-confirm-open?_ true))

(defn- confirm-resolve [res]
  (reset! is-confirm-open?_ false)
  (put! @confirm-ch_ res))

(defn- confirm-ok []
  (confirm-resolve true))

(defn- confirm-cancel []
  (confirm-resolve false))

(defn show-confirm
  "confirm box를 표시한다.
   
   (show-confirm msg)"
  [msg]
  (let [new-ch (chan)]
    (open-confirm msg)
    (reset! confirm-ch_ new-ch)
    @confirm-ch_))

(defn confirm-box [modal-container-selector]
  (let [modal-container (ocall js/document "querySelector" modal-container-selector)]
    (fn []
      (when @is-confirm-open?_ 
        (react-dom/createPortal (r/as-element [:div.modal-container 
                                               [:div.confirm-box
                                                [:p @confirm-msg_]
                                                [:div.button-wrapper 
                                                [:button.btn.confirm-box__buton
                                                  {:on-click confirm-ok}
                                                  "Ok"]
                                                [:button.btn.confirm-box__buton
                                                  {:on-click confirm-cancel}
                                                  "Cancel"]]]]) modal-container)))))