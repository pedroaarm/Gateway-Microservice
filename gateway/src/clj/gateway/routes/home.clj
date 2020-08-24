(ns gateway.routes.home
  (:require
   [gateway.layout :as layout]
   [clojure.java.io :as io]
   [gateway.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]
   [gateway.signInAndUp.signUp :as sign]
   [gateway.credit.purchaseAutorization :as puchase]))

(defn home-page [request]
  (layout/render request "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page [request]
  (layout/render request "about.html"))

(def cliente "/api/cliente")

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   [(str cliente "/cadastro") {:get sign/signUp}]
   [(str cliente "/compra/cartao") {:get puchase/buyWithCreditCard}]
    [(str cliente "/compra/boleto") {:get puchase/buyWithSlipPayment}]])

