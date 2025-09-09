# The Mother Plant Backend

*A Spring Boot REST API built to help my mom start her own plant business*

## About This Project

This is the backend for "The Mother Plant" - a web application I created to help my mom sell plants from her garden. What started as a personal family project became a full-featured plant marketplace with AI-powered plant identification and automated content generation.

My mom has always had a green thumb and grows beautiful plants, but she needed a modern way to showcase and sell them online. This application gives her the tools to easily manage her plant inventory, automatically identify unknown plants, and generate professional descriptions and pricing.

## What It Does

**For Plant Sellers (My Mom!):**
- Upload plant photos and automatically identify species using AI
- Generate professional plant descriptions and suggested pricing with Google Gemini AI
- Manage plant inventory with an easy-to-use catalog system
- Organize plants with tags and categories
- Secure admin interface for adding, editing, and removing plants

**For Plant Buyers:**
- Browse a beautiful catalog of available plants
- Filter plants by tags and categories
- View detailed plant information with high-quality photos
- Get information about plant care and characteristics

## Key Features

- **AI Plant Identification**: Integrated with PlantNet API to automatically identify plant species from photos
- **Smart Content Generation**: Uses Google Gemini AI to create engaging plant descriptions and suggest fair pricing
- **Professional Image Storage**: Cloudflare R2 integration for fast, reliable image hosting
- **User Authentication**: Secure JWT-based authentication with role-based access (buyers vs. sellers)
- **Mobile-Friendly API**: RESTful design that works seamlessly with web and mobile frontends

## The Technology

Built with modern, production-ready technologies:
- **Spring Boot 3** with Java 21 for robust backend services
- **PostgreSQL** for reliable data storage
- **JWT Authentication** for secure user management
- **Cloudflare R2** for scalable image storage
- **PlantNet API** for accurate plant identification
- **Google Gemini AI** for intelligent content generation
- **Docker** ready for easy deployment

## Why This Matters

This isn't just another CRUD application - it's a real solution to a real problem. My mom wanted to share her passion for plants and earn some income from her hobby, but traditional e-commerce platforms were too complex and generic. 

This custom solution gives her:
- **Simplicity**: Easy plant management without technical complexity
- **Professional Results**: AI-generated content that makes her plants look professional
- **Time Savings**: Automated identification and description generation

The application demonstrates practical full-stack development skills while solving a genuine business need for someone I care about.

## Live Demo

Check out the live demo of The Mother Plant backend API: [Live Demo Link](https://mother-plant.vercel.app)