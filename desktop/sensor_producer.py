import csv
import json
import time
import uuid
import pika
import os
from itertools import cycle

def send_data_to_queue(timestamp, idDevice, value, channel):
    data = {
        "timestamp": timestamp,
        "idDevice": idDevice,
        "value": value
    }
    message = json.dumps(data)
    channel.basic_publish(exchange='direct-exchange', routing_key='routing-key', body=message)
    print(f" [x] Sent {message}")

def read_device_ids(file_path):
    with open(file_path, 'r') as ids_file:
        device_ids = [line.strip() for line in ids_file]
    return device_ids

def read_csv_and_send_data(device_ids):
    url = os.environ.get('CLOUDAMQP_URL', 'amqps://urgoblzr:HwN8gfT5ahpy_vq0SAprnoC9EDWda0Dz@goose.rmq2.cloudamqp.com/urgoblzr')
    params = pika.URLParameters(url)
    connection = pika.BlockingConnection(params)
    channel = connection.channel()
    channel.queue_declare(queue='energy_producer_queue')

    with open('sensor.csv', 'r') as csv_file:
        csv_reader = csv.reader(csv_file)

        device_id_cycle = cycle(device_ids)

        for row in csv_reader:
            timestamp = int(time.time() * 1000)
            value = float(row[0])
            
            idDevice = next(device_id_cycle)

            send_data_to_queue(timestamp, idDevice, value, channel)
            time.sleep(10)

    connection.close()

if __name__ == '__main__':
    device_ids = read_device_ids('device_ids.txt')
    read_csv_and_send_data(device_ids)