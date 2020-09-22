(ns app.components.demo-app
  (:require [reagent.core :as r]
            [cljs.core.async :as ca :refer [go <!]]
            [app.components.confirm :refer [show-confirm]]))


(defn demo-app []
  (let [input-txt (r/atom "")
        confirm-result (r/atom nil)]
    (fn []
       [:div.demo__container
        [:h2 "아래 버튼을 눌러보세요"]
        [:input.demo__input
         {:placeholder "Confirm 창에 띄울 메시지를 입력"
          :value @input-txt
          :on-change (fn [^js e]
                       (reset! input-txt (-> e .-target .-value)))}]
        (when (not (nil? @confirm-result)) 
          [:h2 (if @confirm-result
                 "확인 되었습니다"
                 "취소 되었습니다")])
        [:div.demo__button-wrapper 
         [:button.btn
          {:on-click (fn []
                       (go
                         (let [res (<! (show-confirm (if (= "" @input-txt)
                                                       "확인창 기본 메시지"
                                                       @input-txt)))]
                           (case res
                             true (do (js/console.log "확인이 되었습니다.")
                                      (reset! confirm-result true))
                             false (do (js/console.log "취소가 되었을 땐")
                                       (reset! confirm-result false))))))}
          "Open Confirm box"]]])))