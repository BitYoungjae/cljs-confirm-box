(ns app.components.confirm
  (:require [reagent.core :as r]
            [react-dom]
            [clojure.core.async :refer [chan put!]]
            [oops.core :refer [ocall]]))

(def ^:private is-confirm-open?_ (r/atom false))
(def ^:private confirm-msg_ (r/atom ""))
(def ^:private confirm-ch (atom nil))

(defn- open-confirm [msg]
  (reset! confirm-msg_ msg)
  (reset! is-confirm-open?_ true))

(defn- confirm-resolve [res]
  (reset! is-confirm-open?_ false)
  (put! @confirm-ch res)
  )

(defn- confirm-ok []
  (confirm-resolve true))

(defn- confirm-cancel []
  (confirm-resolve false))

(defn show-confirm
  "confirm box를 표시한다.
   map 형태의 매개변수를 받으며, 아래의 형태를 갖는다.
   
   (show-confirm {:msg '컨펌박스에 출력할 메시지'
                  :on-confirm #(컨펌 되었을 때 실행할 함수)})"
  [msg]
  (let [new-ch (chan)]
    (open-confirm msg)
    (reset! confirm-ch new-ch)
    @confirm-ch))

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