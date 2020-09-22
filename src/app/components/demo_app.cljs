(ns app.components.demo-app
  (:require [reagent.core :as r]
            [cljs.core.async :as ca :refer [go <!]]
            [app.components.confirm :refer [show-confirm]]))


(defn demo-app []
  (let [input-txt (r/atom "")]
    (fn []
       [:div.demo__container
        [:h2 "아래 버튼을 눌러보세요"]
        [:input.demo__input
         {:placeholder "Confirm 창에 띄울 메시지를 입력"
          :value @input-txt
          :on-change (fn [^js e]
                       (reset! input-txt (-> e .-target .-value)))}]
        [:div.demo__button-wrapper 
         [:button.btn
          {:on-click (fn []
                       (go
                         (let [res (<! (show-confirm (if (= "" @input-txt)
                                                       "확인창 기본 메시지"
                                                       @input-txt)))]
                           (case res
                             true (do (js/console.log "확인이 되었을 때만")
                                      (js/console.log "여기서 무슨 동작을 합니다."))
                             false (do (js/console.log "취소가 되었을 땐")
                                       (js/console.log "여기서 무슨 동작을 또 합니다."))))))}
          "Open Confirm box"]]])))