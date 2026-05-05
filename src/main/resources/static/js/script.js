// script.js

let ytPlayer;
let progressInterval;
let currentYoutubeId;
let currentCourseId;
let currentLessonId;

// Load YouTube IFrame API
const tag = document.createElement('script');
tag.src = "https://www.youtube.com/iframe_api";
const firstScriptTag = document.getElementsByTagName('script')[0];
if (firstScriptTag) {
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
} else {
    document.head.appendChild(tag);
}

window.onYouTubeIframeAPIReady = function() {
    // API loaded
};

document.addEventListener('DOMContentLoaded', function() {
    // Handle enroll button
    const enrollBtn = document.getElementById('enroll-btn');
    if (enrollBtn) {
        enrollBtn.addEventListener('click', function() {
            const courseId = this.getAttribute('data-course-id');
            enrollInCourse(courseId);
        });
    }

    // Initialize course detail page
    if (document.getElementById('lessons-section')) {
        const courseDetail = document.querySelector('.course-detail');
        const courseId = document.getElementById('enroll-btn')?.getAttribute('data-course-id');
        
        // Check if enrolled on backend by checking if completed-lessons exists and isn't empty, or if backend-progress > 0
        const backendProgress = parseInt(courseDetail?.getAttribute('data-backend-progress') || '0');
        const completedLessonsStr = courseDetail?.getAttribute('data-completed-lessons') || '[]';
        const isEnrolledOnBackend = backendProgress > 0 || completedLessonsStr !== '[]';
        
        if (courseId && isEnrolledOnBackend) {
            showLessonsSection(courseId);
        }
    }

    // Load related videos for course
    const courseDetail = document.querySelector('.course-detail');
    if (courseDetail) {
        const courseTopic = courseDetail.getAttribute('data-course-topic') || 'programming';
        loadRelatedVideos(courseTopic);
    }

    // Search functionality
    const searchInput = document.getElementById('course-search');
    if (searchInput) {
        searchInput.addEventListener('input', function(e) {
            const searchTerm = e.target.value.toLowerCase();
            const courseCards = document.querySelectorAll('.course-card');
            
            courseCards.forEach(card => {
                const title = card.getAttribute('data-title')?.toLowerCase() || '';
                const desc = card.getAttribute('data-description')?.toLowerCase() || '';
                const instructor = card.getAttribute('data-instructor')?.toLowerCase() || '';
                
                if (title.includes(searchTerm) || desc.includes(searchTerm) || instructor.includes(searchTerm)) {
                    card.style.display = 'flex';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    }
});

// Toast notification function
function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    // Add icon based on type
    const icon = type === 'success' ? '✅' : 'ℹ️';
    
    toast.innerHTML = `
        <span class="toast-icon">${icon}</span>
        <span class="toast-message">${message}</span>
    `;
    
    container.appendChild(toast);
    
    // Trigger animation
    setTimeout(() => toast.classList.add('show'), 10);
    
    // Remove after 3 seconds
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

function enrollInCourse(courseId) {
    fetch(`/api/progress/update?courseId=${courseId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    }).then(response => {
        if (response.ok) {
            showToast('Successfully enrolled in the course!', 'success');
            // Show lessons section if on course detail page
            if (document.getElementById('lessons-section')) {
                // Set fake data so the UI doesn't crash before refresh
                const courseDetail = document.querySelector('.course-detail');
                if (courseDetail) {
                    courseDetail.setAttribute('data-completed-lessons', '[]');
                    courseDetail.setAttribute('data-backend-progress', '0');
                }
                showLessonsSection(courseId);
            }
        } else {
            showToast('Failed to enroll. Please try logging in again.', 'info');
        }
    }).catch(err => {
        console.error(err);
        showToast('Error enrolling in course.', 'info');
    });
}

// loadMyCourses removed as it's now handled entirely by the backend and Thymeleaf

function showLessonsSection(courseId) {
    const lessonsSection = document.getElementById('lessons-section');
    const enrollBtn = document.getElementById('enroll-btn');

    if (enrollBtn) {
        enrollBtn.style.display = 'none';
    }
    if (lessonsSection) {
        lessonsSection.style.display = 'block';
    }

    const lessonItems = document.querySelectorAll('.lesson-item');
    
    lessonItems.forEach(lessonDiv => {
        const lessonId = lessonDiv.getAttribute('data-lesson-id');
        
        if (isLessonCompleted(courseId, lessonId)) {
            lessonDiv.classList.add('completed');
        }
        
        // Prevent duplicate event listeners
        const newLessonDiv = lessonDiv.cloneNode(true);
        lessonDiv.parentNode.replaceChild(newLessonDiv, lessonDiv);
        
        newLessonDiv.addEventListener('click', function() {
            const youtubeId = this.getAttribute('data-youtube-id');
            const title = this.getAttribute('data-title');
            const description = this.getAttribute('data-description');
            
            showVideoPanel(this, courseId, lessonId, youtubeId, title, description);
            
            const existingProgress = getVideoProgress(youtubeId);
            updateVideoProgressUI(existingProgress);
        });
    });

    updateCourseProgress(courseId);
}

function isLessonCompleted(courseId, lessonId) {
    const courseDetail = document.querySelector('.course-detail');
    if (!courseDetail) return false;
    const completedStr = courseDetail.getAttribute('data-completed-lessons');
    if (!completedStr || completedStr === '[]') return false;
    try {
        const completedIds = JSON.parse(completedStr);
        return completedIds.includes(parseInt(lessonId));
    } catch(e) {
        return false;
    }
}

function toggleLessonCompletion(courseId, lessonId, completed) {
    // Handled by backend now
}

function updateCourseProgress(courseId) {
    const lessonItems = document.querySelectorAll('.lesson-item');
    const totalLessons = lessonItems.length || 4; 
    let completedCount = 0;
    
    // Update lesson items visually based on backend data initially
    document.querySelectorAll('.lesson-item').forEach(item => {
        const lessonId = item.getAttribute('data-lesson-id');
        if (isLessonCompleted(courseId, lessonId) || item.classList.contains('completed')) {
            item.classList.add('completed');
            completedCount++;
        }
    });

    const progress = totalLessons === 0 ? 0 : Math.round((completedCount / totalLessons) * 100);

    // Update progress bar on course detail page
    const progressFill = document.getElementById('course-progress-fill');
    const progressText = document.getElementById('course-progress-text');

    if (progressFill && progressText) {
        progressFill.style.width = `${progress}%`;
        progressText.textContent = `${progress}% Complete`;
    }

    const claimBtn = document.getElementById('claim-certificate-btn');
    if (claimBtn) {
        if (progress === 100) {
            claimBtn.style.display = 'inline-block';
        } else {
            claimBtn.style.display = 'none';
        }
    }
}

// Deprecated mock progress functions removed

function markLessonViewed(courseId, lessonId) {
    const courseDetail = document.querySelector('.course-detail');
    if (!courseDetail) return;
    
    let completedStr = courseDetail.getAttribute('data-completed-lessons');
    let completedIds = [];
    if (completedStr && completedStr !== '[]') {
        try { completedIds = JSON.parse(completedStr); } catch(e){}
    }
    
    const id = parseInt(lessonId, 10);

    if (!completedIds.includes(id)) {
        completedIds.push(id);
        courseDetail.setAttribute('data-completed-lessons', JSON.stringify(completedIds));
        
        // Sync with backend immediately with the lessonId
        fetch(`/api/progress/update?courseId=${courseId}&lessonId=${lessonId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        }).catch(err => console.log('Failed to sync lesson progress to server:', err));
    }
}

function showVideoPanel(clickedElement, courseId, lessonId, youtubeId, title, description) {
    const panel = document.getElementById('video-player-panel');
    const videoTitle = document.getElementById('video-lesson-title');
    const videoDesc = document.getElementById('video-description');

    if (panel && videoTitle && videoDesc && clickedElement) {
        currentCourseId = courseId;
        currentLessonId = lessonId;
        currentYoutubeId = youtubeId;
        videoTitle.textContent = title;
        videoDesc.textContent = description;
        
        // Move the panel immediately after the clicked element
        clickedElement.parentNode.insertBefore(panel, clickedElement.nextSibling);
        
        panel.style.display = 'flex';

        // Initialize or update YouTube Player
        if (window.YT && window.YT.Player) {
            if (ytPlayer) {
                ytPlayer.loadVideoById(youtubeId);
            } else {
                ytPlayer = new YT.Player('youtube-video', {
                    videoId: youtubeId,
                    width: '100%',
                    height: '100%',
                    playerVars: { 'autoplay': 1, 'controls': 1, 'rel': 0 },
                    events: {
                        'onStateChange': onPlayerStateChange
                    }
                });
            }
        }

        // Load existing progress for this video
        const existingProgress = getVideoProgress(youtubeId);
        updateVideoProgressUI(existingProgress);

        // Scroll to video smoothly
        panel.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
}

function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.PLAYING) {
        if (progressInterval) clearInterval(progressInterval);
        progressInterval = setInterval(trackVideoProgress, 1000);
    } else {
        if (progressInterval) clearInterval(progressInterval);
    }
}

function trackVideoProgress() {
    if (ytPlayer && ytPlayer.getCurrentTime && currentYoutubeId) {
        const currentTime = ytPlayer.getCurrentTime();
        const duration = ytPlayer.getDuration();
        if (duration > 0) {
            let progressPercent = Math.round((currentTime / duration) * 100);
            
            // Prevent progress from going backwards if they scrub back
            const savedProgress = getVideoProgress(currentYoutubeId);
            if (progressPercent < savedProgress && savedProgress !== 100) {
                progressPercent = savedProgress;
            }
            
            updateVideoProgressUI(progressPercent);
            localStorage.setItem(`videoProgress_${currentYoutubeId}`, progressPercent);

            // Mark lesson as completed if watched >= 99%
            if (progressPercent >= 99 && currentCourseId && currentLessonId) {
                markLessonViewed(currentCourseId, currentLessonId);
                updateCourseProgress(currentCourseId);
            }
        }
    }
}

function updateVideoProgressUI(progressPercent) {
    const progressFill = document.getElementById('video-progress-fill');
    const progressText = document.getElementById('video-progress-text');

    if (progressFill && progressText) {
        progressFill.style.width = progressPercent + '%';
        progressText.textContent = progressPercent + '% Watched';
    }
}

function getVideoProgress(youtubeId) {
    return parseInt(localStorage.getItem(`videoProgress_${youtubeId}`)) || 0;
}

function loadRelatedVideos(topic) {
    const relatedVideos = getRelatedVideos(topic);
    const relatedVideosList = document.getElementById('related-videos-list');
    if (!relatedVideosList) return;

    relatedVideosList.innerHTML = '';

    relatedVideos.forEach(video => {
        const videoItem = document.createElement('div');
        videoItem.className = 'related-video-item';
        videoItem.innerHTML = `
            <div class="related-video-thumbnail">
                <img src="https://img.youtube.com/vi/${video.youtubeId}/mqdefault.jpg" alt="${video.title}" loading="lazy">
                <div class="related-video-play-overlay">
                    <span>▶</span>
                </div>
            </div>
            <div class="related-video-info">
                <h5>${video.title}</h5>
                <p>${video.description}</p>
            </div>
        `;
        videoItem.addEventListener('click', () => {
            const courseIntroVideo = document.getElementById('course-intro-video');
            if (courseIntroVideo) {
                courseIntroVideo.src = `https://www.youtube.com/embed/${video.youtubeId}?autoplay=1`;
            }
        });
        relatedVideosList.appendChild(videoItem);
    });
}

function getRelatedVideos(query) {
    const normalized = query.toLowerCase();

    const javaVideos = [
        { youtubeId: 'eIrMbAQSU34', title: 'Java Programming Tutorial for Beginners', description: 'Start learning Java from scratch.' },
        { youtubeId: 'O3gDStImKW0', title: 'Java Object Oriented Programming', description: 'Understand OOP concepts in Java.' },
        { youtubeId: 'jFDg38XdsGo', title: 'Java Collections Framework', description: 'Learn how Java collections work.' },
        { youtubeId: 'vBc7T7d_yWk', title: 'Java Arrays and Data Structures', description: 'Working with arrays in Java.' }
    ];

    const springVideos = [
        { youtubeId: 'vtPkZShrvXQ', title: 'Spring Framework Full Course', description: 'A complete Spring Framework walkthrough.' },
        { youtubeId: 'KbhmfKjPR2M', title: 'Spring MVC Crash Course', description: 'Build web apps using Spring MVC.' },
        { youtubeId: '9SGDpanrc8U', title: 'Spring Boot REST API Tutorial', description: 'Create REST APIs with Spring Boot.' },
        { youtubeId: 'her_7pa0vrg', title: 'Spring Security Essentials', description: 'Secure your Spring applications.' }
    ];

    const dataVideos = [
        { youtubeId: '0uZvQZGXn9M', title: 'Linked Lists Explained', description: 'Understanding linked list data structure.' },
        { youtubeId: 'Gp2bVxW5rXc', title: 'Stacks and Queues in Data Structures', description: 'Learn stack and queue implementations.' },
        { youtubeId: 'bUNkvhFXfWw', title: 'Sorting Algorithms', description: 'Common sorting algorithms explained.' },
        { youtubeId: 'vBc7T7d_yWk', title: 'Arrays in Programming', description: 'Working with arrays and strings.' }
    ];

    const mlVideos = [
        { youtubeId: 'ukzFI9rgwfU', title: 'Machine Learning Basics', description: 'Intro to machine learning concepts.' },
        { youtubeId: 'cfj6yaYE86U', title: 'Supervised Learning Explained', description: 'Understand supervised learning algorithms.' },
        { youtubeId: '1XqG0kaJVHY', title: 'Unsupervised Learning for Beginners', description: 'Explore clustering techniques.' },
        { youtubeId: 'Vpcl3DlP2xo', title: 'Model Evaluation Techniques', description: 'How to evaluate ML models.' }
    ];

    const generalProgramming = [
        { youtubeId: 'Zxwq3aW9ctU', title: 'Programming Fundamentals', description: 'Essential programming concepts.' },
        { youtubeId: 'eIrMbAQSU34', title: 'Coding for Beginners', description: 'Start your programming journey.' },
        { youtubeId: 'vtPkZShrvXQ', title: 'Web Development Basics', description: 'Learn web development fundamentals.' },
        { youtubeId: 'ukzFI9rgwfU', title: 'Algorithm Design', description: 'Understanding algorithms and data structures.' }
    ];

    if (normalized.includes('spring')) {
        return springVideos;
    }
    if (normalized.includes('machine learning') || normalized.includes('ml') || normalized.includes('model')) {
        return mlVideos;
    }
    if (normalized.includes('array') || normalized.includes('linked list') || normalized.includes('data structure') || normalized.includes('algorithm') || normalized.includes('queue') || normalized.includes('stack')) {
        return dataVideos;
    }
    if (normalized.includes('java')) {
        return javaVideos;
    }

    return generalProgramming;
}