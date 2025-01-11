from asyncio import start_server, run

"""
to test:
1. telnet localhost 8888
2. curl --raw telnet://localhost:8888
3. ./echo_tester.py
"""

async def on_client_connected(reader, writer):
    while True:
        data = await reader.readline()
        if not data:
            break
        writer.write(data)

async def server():
    srv = await start_server(on_client_connected, '127.0.0.1', 8888)
    async with srv:
        await srv.serve_forever()

run(server())
