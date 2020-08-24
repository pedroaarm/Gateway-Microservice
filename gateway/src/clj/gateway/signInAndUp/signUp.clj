(ns gateway.signInAndUp.signUp
  (:require
   [clojure.java.io :as io]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [ring.util.http-response :as response]
   [ring.util.request :as request]
   [ring.util.response :refer [redirect]]
   [ring.util.response :refer [file-response]]
   [gateway.credit.generateCreditCard :as creditCard]
   [gateway.database.paymentDatabase :as database]
   [gateway.credit.defineCredit :as credit]))

(defn- returDataClient [cpf]
  (-> 
   (database/returnDatanewCard cpf)))

(defn signUp [request]
  (println "endtour---> " (get-in request [:params :cpf]))
 (try 
  (let [id-client (database/insertnewclienteprincipal
                   (get-in request [:params :cpf])
                   (get-in request [:params :email])
                   (get-in request [:params :brith])
                   (get-in request [:params :name]))
        
        creditCard (creditCard/defineNumberCreditCard (get-in request [:params :cpf])
                     (get-in request [:params :brith]))
        
        password (creditCard/generateAleatoryPassword)
        cardLimit (credit/defineLimit (get-in request [:params :salary]))]
    
    (future (database/insertCredtCartInformations (get-in request [:params :cpf])
                                                  creditCard password cardLimit cardLimit))
    (database/insertSalary (get-in request [:params :cpf])
                           (get-in request [:params :profession])
                           (get-in request [:params :salary]))
    (response/ok (returDataClient (get-in request [:params :cpf])))
    )
(catch Exception e
  (println e)
        (response/bad-request "erro"))))