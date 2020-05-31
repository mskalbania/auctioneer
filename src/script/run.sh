docker rm -f psql
docker run --name psql -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=auctioneer_db -p 5432:5432 -d postgres