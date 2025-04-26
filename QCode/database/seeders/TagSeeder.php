<?php

namespace Database\Seeders;

use App\Models\Tag;
use Illuminate\Database\Seeder;

class TagSeeder extends Seeder
{
    public function run()
    {
        $tags = [
            ['name' => 'php', 'description' => 'PHP programming language'],
            ['name' => 'laravel', 'description' => 'Laravel framework'],
            ['name' => 'mysql', 'description' => 'MySQL database'],
            ['name' => 'javascript', 'description' => 'JavaScript programming language'],
            ['name' => 'html', 'description' => 'HTML markup language'],
            ['name' => 'css', 'description' => 'CSS styling'],
            ['name' => 'vue.js', 'description' => 'Vue.js framework'],
            ['name' => 'react', 'description' => 'React library'],
            ['name' => 'database', 'description' => 'Database related questions'],
            ['name' => 'security', 'description' => 'Security related questions'],
        ];

        foreach ($tags as $tag) {
            Tag::firstOrCreate(['name' => $tag['name']], $tag);
        }
    }
}