To run the project, import it using maven, into your preferred IDE

Within the project there is an "infra" folder with docker-compose which is necessary to upload the database (postgres) and a kafka broker.


## curl to create purchase transaction ##
curl --location 'http://localhost:8080/transaction' \
--header 'Content-Type: application/json' \
--data '{
    "description": "Brazil test",
    "transactionDate": "2022-10-01",
    "amount": 100.00,
    "transaction": "80360c9c-6f51-443e-bac1-8e7486d6e59b"
}'


## curl to retrieve a purchase transaction in specified country ##
curl --location 'http://localhost:8080/hello?transactionId=4e8b415a-0ee9-4473-8a78-434305bfe1c2&country=brazil&transactionDate=2023-10-01'
