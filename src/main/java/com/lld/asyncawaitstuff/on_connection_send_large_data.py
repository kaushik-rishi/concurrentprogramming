import asyncio
import psutil
import os
from datetime import datetime

def get_memory_usage():
    """Get current memory usage in MB"""
    process = psutil.Process(os.getpid())
    return process.memory_info().rss / 1024 / 1024

async def monitor_memory():
    initial_memory = get_memory_usage()
    print(f"Initial memory usage: {initial_memory:.2f} MB")

    while True:
        current_memory = get_memory_usage()
        delta = current_memory - initial_memory
        print(f"[{datetime.now().strftime('%H:%M:%S')}] "
              f"Memory: {current_memory:.2f} MB "
              f"(Δ: {'+' if delta >= 0 else ''}{delta:.2f} MB)")
        await asyncio.sleep(0.5)

async def on_client_connected(reader, writer):
    print(f"\n[{datetime.now().strftime('%H:%M:%S')}] Client connected")
    print(f"Memory before data generation: {get_memory_usage():.2f} MB")

    # Generate 50MB of data in increasing chunks
    chunk_sizes = [20]  # MB sizes
    for size in chunk_sizes:
        size_bytes = size * 1024 * 1024  # Convert MB to bytes
        print(f"\nGenerating {size}MB chunk...")
        data = b"X" * size_bytes

        print(f"Memory after generation: {get_memory_usage():.2f} MB")
        print(f"Writing {size}MB to buffer...")
        writer.write(data)
        print(f"Memory after write: {get_memory_usage():.2f} MB")

        print("Waiting for drain...")
        await writer.drain()
        print(f"Memory after drain: {get_memory_usage():.2f} MB")
        del data

        await asyncio.sleep(1)

    writer.close()
    await writer.wait_closed()

async def server():
    # Start memory monitoring as a background task
    monitor = asyncio.create_task(monitor_memory())

    server = await asyncio.start_server(on_client_connected, '127.0.0.1', 7000)
    async with server:
        await server.serve_forever()

if __name__ == "__main__":
    try:
        asyncio.run(server())
    except KeyboardInterrupt:
        print("\nShutting down...")
