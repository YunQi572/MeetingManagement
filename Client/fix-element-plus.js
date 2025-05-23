const fs = require('fs');
const path = require('path');

// 定义要处理的视图目录
const viewDirs = [
    path.join(__dirname, 'src', 'views'),
    path.join(__dirname, 'src', 'views', 'history'),
    path.join(__dirname, 'src', 'views', 'book')
];

// 查找所有Vue文件
function findVueFiles(dir) {
    let results = [];
    const files = fs.readdirSync(dir);

    for (const file of files) {
        const fullPath = path.join(dir, file);
        const stat = fs.statSync(fullPath);

        if (stat.isDirectory()) {
            // 递归处理子目录
            results = results.concat(findVueFiles(fullPath));
        } else if (file.endsWith('.vue')) {
            results.push(fullPath);
        }
    }

    return results;
}

// 修复面包屑组件
function fixBreadcrumb(content) {
    return content.replace(
        /<el-breadcrumb separator-class="el-icon-arrow-right">/g,
        '<el-breadcrumb :separator-icon="ArrowRight">'
    );
}

// 添加导入ArrowRight的语句
function addArrowRightImport(content) {
    // 检查是否已经导入了ArrowRight
    if (content.includes("import { ArrowRight }") || !content.includes('<el-breadcrumb :separator-icon="ArrowRight">')) {
        return content;
    }

    // 添加导入语句
    return content.replace(
        /import\s+{\s*([^}]+)\s*}\s+from\s+['"]element-plus['"]/,
        (match, imports) => {
            if (imports.includes('ArrowRight')) {
                return match; // 已经导入了ArrowRight
            }
            return `import { ${imports}, ArrowRight } from 'element-plus'`;
        }
    ).replace(
        /<script setup>/,
        '<script setup>\nimport { ArrowRight } from \'@element-plus/icons-vue\''
    );
}

// 处理所有视图文件
function processAllFiles() {
    let allVueFiles = [];

    for (const dir of viewDirs) {
        if (fs.existsSync(dir)) {
            allVueFiles = allVueFiles.concat(findVueFiles(dir));
        }
    }

    console.log(`Found ${allVueFiles.length} Vue files to process.`);

    for (const file of allVueFiles) {
        try {
            let content = fs.readFileSync(file, 'utf8');
            const originalContent = content;

            // 应用修复
            content = fixBreadcrumb(content);
            content = addArrowRightImport(content);

            // 只有内容发生变化时才写入文件
            if (content !== originalContent) {
                fs.writeFileSync(file, content, 'utf8');
                console.log(`Updated: ${file}`);
            } else {
                console.log(`No changes needed for: ${file}`);
            }
        } catch (error) {
            console.error(`Error processing file ${file}:`, error);
        }
    }
}

// 执行处理
processAllFiles();
console.log('Element Plus compatibility fixes completed.'); 