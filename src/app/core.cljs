(ns app.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [oops.core :refer [ocall]]
            [cljs.core.async :as ca :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]))

(defn app []
  [:div "Hello world!"])

(defn ^:dev/after-load start [] ; HMR 을 위한 코드 ^:dev~는 메타 태그이다
  ; 매번 파일 저장시마다 이 함수가 호출이 된다.
  (let [app-container (ocall js/document "querySelector" "#app")]
    (rdom/render app app-container)))

(defn ^:export init [] ; entry point. devserver 실행시 호출된다.
  (start))