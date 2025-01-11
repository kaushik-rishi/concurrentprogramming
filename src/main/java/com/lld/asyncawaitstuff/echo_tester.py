import asyncio

async def test_client():
    reader, writer = await asyncio.open_connection('127.0.0.1', 8888)

    print('Sending: Hello!')
    writer.write(b'Hello!\n')
    await writer.drain()

    data = await reader.readline()
    print(f'Received: {data.decode()}')

    writer.close()
    await writer.wait_closed()

asyncio.run(test_client())
