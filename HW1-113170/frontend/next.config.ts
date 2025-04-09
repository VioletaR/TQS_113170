import type { NextConfig } from "next";

/** @type {import('next').NextConfig} */
const nextConfig:NextConfig = {
    allowedDevOrigins: ['localhost', '*.localhost', '0.0.0.0'],
    output: "standalone"
}


export default nextConfig;
