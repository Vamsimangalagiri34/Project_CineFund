// API Configuration - Hybrid approach
const USER_SERVICE_URL = 'http://localhost:8084';  // Direct to User Service
const API_GATEWAY_URL = 'http://localhost:8090';   // Through API Gateway for other services

// API endpoints
const API_ENDPOINTS = {
    // User Service
    USERS: {
        REGISTER: '/api/users/register',
        LOGIN: '/api/users/login',
        PROFILE: '/api/users',
        BY_ID: (id) => `/api/users/${id}`,
        BY_USERNAME: (username) => `/api/users/username/${username}`,
        BY_ROLE: (role) => `/api/users/role/${role}`,
        UPDATE: (id) => `/api/users/${id}`,
        WALLET: (id) => `/api/users/${id}/wallet`,
        SEARCH: '/api/users/search'
    },
    // Movie Service
    MOVIES: {
        ALL: '/api/movies',
        BY_ID: (id) => `/api/movies/${id}`,
        FUNDING: '/api/movies/funding',
        BY_PRODUCER: (producerId) => `/api/movies/producer/${producerId}`,
        BY_STATUS: (status) => `/api/movies/status/${status}`,
        BY_GENRE: (genre) => `/api/movies/genre/${genre}`,
        CREATE: '/api/movies',
        UPDATE: (id) => `/api/movies/${id}`,
        UPDATE_STATUS: (id) => `/api/movies/${id}/status`,
        UPDATE_FUNDING: (id) => `/api/movies/${id}/funding`,
        SEARCH: '/api/movies/search',
        BUDGET_RANGE: '/api/movies/budget-range'
    },
    // Funding Service
    FUNDING: {
        INVEST: '/api/funding/invest',
        CONFIRM: (transactionId) => `/api/funding/confirm/${transactionId}`,
        CANCEL: (transactionId) => `/api/funding/cancel/${transactionId}`,
        BY_ID: (id) => `/api/funding/investment/${id}`,
        BY_TRANSACTION: (transactionId) => `/api/funding/transaction/${transactionId}`,
        BY_USER: (userId) => `/api/funding/user/${userId}`,
        BY_MOVIE: (movieId) => `/api/funding/movie/${movieId}`,
        BY_PRODUCER: (producerId) => `/api/funding/producer/${producerId}`,
        CONFIRMED_BY_MOVIE: (movieId) => `/api/funding/movie/${movieId}/confirmed`,
        USER_MOVIES: (userId) => `/api/funding/user/${userId}/movies`,
        PROCESS_RETURNS: (movieId) => `/api/funding/returns/${movieId}`,
        UNPAID_RETURNS: '/api/funding/returns/unpaid',
        UNPAID_BY_MOVIE: (movieId) => `/api/funding/returns/unpaid/movie/${movieId}`,
        PRODUCER_RETURNS: (producerId, movieId) => `/api/funding/producer/${producerId}/movie/${movieId}/returns`,
        PRODUCER_ALL_RETURNS: (producerId) => `/api/funding/producer/${producerId}/returns/bulk`,
        PRODUCER_RETURN_SUMMARY: (producerId) => `/api/funding/producer/${producerId}/returns/summary`
    }
};

// HTTP utility functions
class ApiService {
    constructor() {
        this.token = localStorage.getItem('authToken');
    }

    // Set authentication token
    setToken(token) {
        this.token = token;
        if (token) {
            localStorage.setItem('authToken', token);
        } else {
            localStorage.removeItem('authToken');
        }
    }

    // Get authentication headers
    getHeaders(contentType = 'application/json') {
        const headers = {
            'Content-Type': contentType
        };
        
        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }
        
        return headers;
    }

    // Generic request method with dynamic base URL
    async request(endpoint, options = {}, useGateway = false) {
        const baseUrl = useGateway ? API_GATEWAY_URL : USER_SERVICE_URL;
        const url = `${baseUrl}${endpoint}`;
        const config = {
            headers: this.getHeaders(),
            ...options
        };

        console.log('Making API request to:', url);
        console.log('Using Gateway:', useGateway);
        console.log('Request config:', config);
        console.log('Request body:', options.body);

        try {
            const response = await fetch(url, config);
            
            // Check if response is JSON
            const contentType = response.headers.get('content-type');
            let data;
            
            if (contentType && contentType.includes('application/json')) {
                data = await response.json();
            } else {
                const text = await response.text();
                console.error('Non-JSON response received:', text);
                throw new Error(`Server returned non-JSON response: ${text.substring(0, 100)}`);
            }

            console.log('API response:', { status: response.status, data });

            if (!response.ok) {
                throw new Error(data.message || `HTTP error! status: ${response.status}`);
            }

            return data;
        } catch (error) {
            console.error('API request failed:', {
                url,
                error: error.message,
                stack: error.stack
            });
            
            // Provide more specific error messages
            if (error.name === 'TypeError' && error.message.includes('fetch')) {
                throw new Error('Cannot connect to server. Please ensure the API Gateway is running on port 8090.');
            }
            
            throw error;
        }
    }


    // POST request
    async post(endpoint, data, useGateway = false) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        }, useGateway);
    }

    // PUT request
    async put(endpoint, data, useGateway = false) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        }, useGateway);
    }

    // DELETE request
    async delete(endpoint, useGateway = false) {
        return this.request(endpoint, { method: 'DELETE' }, useGateway);
    }

    // GET request
    async get(endpoint, useGateway = false) {
        return this.request(endpoint, { method: 'GET' }, useGateway);
    }

    // User API methods
    async registerUser(userData) {
        return this.post(API_ENDPOINTS.USERS.REGISTER, userData);
    }

    async loginUser(credentials) {
        const response = await this.post(API_ENDPOINTS.USERS.LOGIN, credentials);
        if (response.success && response.token) {
            this.setToken(response.token);
        }
        return response;
    }

    async getUserById(id) {
        return this.get(API_ENDPOINTS.USERS.BY_ID(id));
    }

    async getUserByUsername(username) {
        return this.get(API_ENDPOINTS.USERS.BY_USERNAME(username));
    }

    async getAllUsers() {
        return this.get(API_ENDPOINTS.USERS.PROFILE);
    }

    async getUsersByRole(role) {
        return this.get(API_ENDPOINTS.USERS.BY_ROLE(role));
    }

    async updateUser(id, userData) {
        return this.put(API_ENDPOINTS.USERS.UPDATE(id), userData);
    }

    async updateWalletBalance(id, amount) {
        return this.put(`${API_ENDPOINTS.USERS.WALLET(id)}?amount=${amount}`);
    }

    async searchUsers(keyword) {
        return this.get(`${API_ENDPOINTS.USERS.SEARCH}?keyword=${encodeURIComponent(keyword)}`);
    }

    // Movie API methods (through API Gateway)
    async getAllMovies() {
        return this.get(API_ENDPOINTS.MOVIES.ALL, true); // Use API Gateway
    }

    async getMovieById(id) {
        return this.get(API_ENDPOINTS.MOVIES.BY_ID(id), true); // Use API Gateway
    }

    async getMoviesForFunding() {
        return this.get(API_ENDPOINTS.MOVIES.FUNDING, true); // Use API Gateway
    }

    async getMoviesByProducer(producerId) {
        return this.get(API_ENDPOINTS.MOVIES.BY_PRODUCER(producerId));
    }

    async getMoviesByStatus(status) {
        return this.get(API_ENDPOINTS.MOVIES.BY_STATUS(status));
    }

    async getMoviesByGenre(genre) {
        return this.get(API_ENDPOINTS.MOVIES.BY_GENRE(genre));
    }

    async createMovie(movieData) {
        return this.post(API_ENDPOINTS.MOVIES.CREATE, movieData);
    }

    async updateMovie(id, movieData) {
        return this.put(API_ENDPOINTS.MOVIES.UPDATE(id), movieData);
    }

    async updateMovieStatus(id, status) {
        return this.put(`${API_ENDPOINTS.MOVIES.UPDATE_STATUS(id)}?status=${status}`);
    }

    async updateMovieFunding(id, amount) {
        return this.put(`${API_ENDPOINTS.MOVIES.UPDATE_FUNDING(id)}?amount=${amount}`);
    }

    async searchMovies(keyword) {
        return this.get(`${API_ENDPOINTS.MOVIES.SEARCH}?keyword=${encodeURIComponent(keyword)}`);
    }

    async getMoviesByBudgetRange(minBudget, maxBudget) {
        return this.get(`${API_ENDPOINTS.MOVIES.BUDGET_RANGE}?minBudget=${minBudget}&maxBudget=${maxBudget}`);
    }

    async investInMovie(userId, investmentData) {
        return this.post(`/api/users/${userId}/invest`, investmentData); // Direct to User Service
    }

    // Funding API methods (through API Gateway)
    async getInvestmentsByMovie(movieId) {
        return this.get(`/api/funding/movie/${movieId}`, true); // Use API Gateway
    }

    async getInvestmentsByProducer(producerId) {
        return this.get(`/api/funding/producer/${producerId}`, true); // Use API Gateway
    }

    async getInvestorsForProducer(producerId) {
        return this.get(`/api/funding/producer/${producerId}/investors`, true); // Use API Gateway
    }

    async getInvestorsForProducerMovie(producerId, movieId) {
        return this.get(`/api/funding/producer/${producerId}/movie/${movieId}/investors`, true); // Use API Gateway
    }

    async getUserInvestments(userId) {
        return this.get(`/api/funding/user/${userId}`, true); // Use API Gateway
    }

    async confirmInvestment(transactionId) {
        return this.put(API_ENDPOINTS.FUNDING.CONFIRM(transactionId));
    }

    async createInvestment(investmentData) {
        return this.post(API_ENDPOINTS.FUNDING.INVEST, investmentData);
    }

    async cancelInvestment(transactionId, reason) {
        return this.put(`${API_ENDPOINTS.FUNDING.CANCEL(transactionId)}?reason=${encodeURIComponent(reason || '')}`);
    }

    async getInvestmentById(id) {
        return this.get(API_ENDPOINTS.FUNDING.BY_ID(id));
    }

    async getInvestmentByTransaction(transactionId) {
        return this.get(API_ENDPOINTS.FUNDING.BY_TRANSACTION(transactionId));
    }

    async getInvestmentsByUser(userId) {
        return this.get(API_ENDPOINTS.FUNDING.BY_USER(userId));
    }

    async getInvestmentsByMovie(movieId) {
        return this.get(API_ENDPOINTS.FUNDING.BY_MOVIE(movieId));
    }

    async getInvestmentsByProducer(producerId) {
        return this.get(API_ENDPOINTS.FUNDING.BY_PRODUCER(producerId));
    }

    async getConfirmedInvestmentsByMovie(movieId) {
        return this.get(API_ENDPOINTS.FUNDING.CONFIRMED_BY_MOVIE(movieId));
    }

    async getUserMovieInvestments(userId) {
        return this.get(API_ENDPOINTS.FUNDING.USER_MOVIES(userId));
    }

    async processReturns(movieId, totalRevenue) {
        return this.post(`${API_ENDPOINTS.FUNDING.PROCESS_RETURNS(movieId)}?totalRevenue=${totalRevenue}`);
    }

    async getUnpaidReturns() {
        return this.get(API_ENDPOINTS.FUNDING.UNPAID_RETURNS);
    }

    async getUnpaidReturnsByMovie(movieId) {
        return this.get(API_ENDPOINTS.FUNDING.UNPAID_BY_MOVIE(movieId));
    }

    // Producer return payment methods
    async processReturnsForProducer(producerId, movieId, totalRevenue, notes) {
        const requestData = {
            totalRevenue: totalRevenue,
            notes: notes
        };
        return this.post(API_ENDPOINTS.FUNDING.PRODUCER_RETURNS(producerId, movieId), requestData, true); // Use API Gateway
    }

    async processReturnsForAllProducerMovies(producerId, returnData) {
        return this.post(API_ENDPOINTS.FUNDING.PRODUCER_ALL_RETURNS(producerId), returnData, true); // Use API Gateway
    }

    async getProducerReturnSummary(producerId) {
        return this.get(API_ENDPOINTS.FUNDING.PRODUCER_RETURN_SUMMARY(producerId), true); // Use API Gateway
    }

    // Utility methods
    logout() {
        this.setToken(null);
        localStorage.removeItem('currentUser');
    }

    isAuthenticated() {
        return !!this.token;
    }

    getCurrentUser() {
        const userStr = localStorage.getItem('currentUser');
        return userStr ? JSON.parse(userStr) : null;
    }

    setCurrentUser(user) {
        localStorage.setItem('currentUser', JSON.stringify(user));
    }
}

// Create global API service instance
const apiService = new ApiService();

// Export for use in other modules
window.apiService = apiService;
