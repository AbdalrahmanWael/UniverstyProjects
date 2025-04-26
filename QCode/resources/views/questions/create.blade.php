@extends('layouts.app')

@section('title', 'Ask a Question')

@section('additional-styles')
.form-container {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    margin-bottom: 2rem;
}
.tag-checkbox {
    display: inline-block;
    margin-right: 1em;
    margin-bottom: 0.5em;
    padding: 0.5em 1em;
    background: #f8f9fa;
    border-radius: 4px;
    transition: background-color 0.2s;
}
.tag-checkbox:hover {
    background: #e9ecef;
}
.preview {
    margin-top: 1em;
    padding: 1em;
    border: 1px solid #dee2e6;
    border-radius: 4px;
    background: #f8f9fa;
}
.code-preview {
    font-family: monospace;
    background: #f8f9fa;
    padding: 1rem;
    border-radius: 4px;
    margin-top: 0.5rem;
}
.add-tag-form {
    margin-top: 1rem;
    padding: 1rem;
    background: #f8f9fa;
    border-radius: 4px;
}
@endsection

@section('content')
<div class="container">
    <div class="mb-4">
        <a href="{{ route('questions.index') }}" class="text-decoration-none">
            <i class="bi bi-arrow-left"></i> Back to Questions
        </a>
    </div>

    <div class="form-container">
        <h1 class="mb-4">Ask a Question</h1>

        @if($errors->any())
            <div class="alert alert-danger">
                <ul class="mb-0">
                    @foreach($errors->all() as $error)
                        <li>{{ $error }}</li>
                    @endforeach
                </ul>
            </div>
        @endif

        <form method="POST" action="{{ route('questions.store') }}" id="questionForm">
            @csrf
            <div class="form-group mb-4">
                <label for="title" class="form-label">Title</label>
                <input type="text" id="title" name="title" 
                    class="form-control @error('title') is-invalid @enderror"
                    value="{{ old('title') }}" 
                    placeholder="Be specific and imagine you're asking a question to another person"
                    required>
                @error('title')
                    <div class="invalid-feedback">{{ $message }}</div>
                @enderror
            </div>

            <div class="form-group mb-4">
                <label for="body" class="form-label">Body</label>
                <textarea id="body" name="body" rows="10" 
                    class="form-control @error('body') is-invalid @enderror"
                    placeholder="Include all the information someone would need to answer your question"
                    required>{{ old('body') }}</textarea>
                @error('body')
                    <div class="invalid-feedback">{{ $message }}</div>
                @enderror
            </div>

            <div class="form-group mb-4">
                <label for="code_snippet" class="form-label">Code (optional)</label>
                <textarea id="code_snippet" name="code_snippet" rows="6"
                    class="form-control font-monospace"
                    placeholder="Share your code if relevant to the question">{{ old('code_snippet') }}</textarea>
                <div id="code-preview" class="preview" style="display: none;">
                    <pre><code class="language-php"></code></pre>
                </div>
            </div>

            <div class="form-group mb-4">
                <label class="form-label d-block">Tags</label>
                <div class="tag-list mb-3">
                    @foreach($tags as $tag)
                        <label class="tag-checkbox">
                            <input type="checkbox" name="tags[]" value="{{ $tag->id }}"
                                {{ in_array($tag->id, old('tags', [])) ? 'checked' : '' }}>
                            {{ $tag->name }}
                        </label>
                    @endforeach
                </div>
                
                <div class="add-tag-form">
                    <h5>Create New Tag</h5>
                    <div class="row g-2">
                        <div class="col-sm-4">
                            <input type="text" id="newTagName" class="form-control" 
                                placeholder="Tag name" maxlength="30">
                        </div>
                        <div class="col-sm-6">
                            <input type="text" id="newTagDescription" class="form-control" 
                                placeholder="Brief description (optional)" maxlength="255">
                        </div>
                        <div class="col-sm-2">
                            <button type="button" id="addTagBtn" class="btn btn-outline-primary w-100">
                                Add Tag
                            </button>
                        </div>
                    </div>
                    <div id="tagError" class="invalid-feedback d-none mt-2"></div>
                </div>

                @error('tags')
                    <div class="invalid-feedback d-block">{{ $message }}</div>
                @enderror
            </div>

            <div class="form-group">
                <button type="submit" class="btn btn-primary">Post Your Question</button>
            </div>
        </form>
    </div>
</div>
@endsection

@section('scripts')
<script>
// Live preview for code snippet
const codeInput = document.getElementById('code_snippet');
const preview = document.getElementById('code-preview');
const code = preview.querySelector('code');

codeInput.addEventListener('input', function() {
    if (this.value) {
        code.textContent = this.value;
        preview.style.display = 'block';
        Prism.highlightElement(code);
    } else {
        preview.style.display = 'none';
    }
});

// Add new tag functionality
document.getElementById('addTagBtn').addEventListener('click', function() {
    const nameInput = document.getElementById('newTagName');
    const descriptionInput = document.getElementById('newTagDescription');
    const errorDiv = document.getElementById('tagError');
    const tagList = document.querySelector('.tag-list');
    
    const name = nameInput.value.trim();
    const description = descriptionInput.value.trim();
    
    if (!name) {
        errorDiv.textContent = 'Tag name is required';
        errorDiv.classList.remove('d-none');
        return;
    }

    fetch('{{ route("tags.store") }}', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').content
        },
        body: JSON.stringify({ name, description })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Add new tag to the list
            const newTag = document.createElement('label');
            newTag.className = 'tag-checkbox';
            newTag.innerHTML = `
                <input type="checkbox" name="tags[]" value="${data.tag.id}" checked>
                ${data.tag.name}
            `;
            tagList.appendChild(newTag);
            
            // Clear inputs and error
            nameInput.value = '';
            descriptionInput.value = '';
            errorDiv.classList.add('d-none');
        }
    })
    .catch(error => {
        errorDiv.textContent = 'Failed to create tag. Please try again.';
        errorDiv.classList.remove('d-none');
    });
});
</script>
@endsection