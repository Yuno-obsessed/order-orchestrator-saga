up-services:
	docker-compose --profile prod up -d --build && \
	docker exec connect ./check_installed_plugins.sh && \
	docker exec connect ./debezium_connector_account.sh && \
	docker exec connect ./debezium_connector_order.sh

down-services:
	docker-compose --profile prod down -v